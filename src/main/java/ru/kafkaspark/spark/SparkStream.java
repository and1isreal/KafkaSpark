package ru.kafkaspark.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kafkaspark.model.Limit;
import ru.kafkaspark.service.LimitService;
import ru.kafkaspark.service.SenderService;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class SparkStream implements Serializable {

    @Autowired
    private transient LimitService limitService;

    @Autowired
    private transient SenderService senderService;

    private static final String HOST = "localhost";
    private static final Integer PORT = 8015;
    private static final String TOPIC = "alerts";
    private static int num;

    public void startSpark() throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("VerySimpleStreamingApp");
        JavaStreamingContext streamingContext =
                new JavaStreamingContext(conf, Durations.seconds(60 * 5));
        Logger.getRootLogger().setLevel(Level.ERROR);

        JavaReceiverInputDStream<String> lines = streamingContext.socketTextStream(HOST, PORT);

        JavaDStream<Integer> linesDStream = lines.map(new Function<String, Integer>() {
            @Override
            public Integer call(String v1) throws Exception {
                return v1.length();
            }
        }).reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        linesDStream.foreachRDD(new VoidFunction<JavaRDD<Integer>>() {
            @Override
            public void call(JavaRDD<Integer> integerJavaRDD) throws Exception {
                if (integerJavaRDD != null) {
                    List<Integer> collection = integerJavaRDD.collect();
                    if (collection.size() > 0) {
                        num = collection.get(0);
                        checkLimits();
                    }
                }
            }
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }

    private void checkLimits() {
        Optional<Limit> minLimit = limitService.getLimitByName("min");
        Optional<Limit> maxLimit = limitService.getLimitByName("max");
        Limit newLimit;

        if (minLimit.isPresent() && maxLimit.isPresent() && (num < minLimit.get().getValue() || num > maxLimit.get().getValue()) ) {
            newLimit = new Limit();
            newLimit.setTime(new Date());
            if (num < minLimit.get().getValue()) {
                newLimit.setId(1);
                newLimit.setName(minLimit.get().getName());
                newLimit.setValue(num);
            }
            if (num > maxLimit.get().getValue()) {
                newLimit.setId(2);
                newLimit.setName(maxLimit.get().getName());
                newLimit.setValue(num);
            }
            senderService.sendJSON(TOPIC, newLimit);
        }
    }

}
