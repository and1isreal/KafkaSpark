package ru.kafkaspark.app;

import org.apache.log4j.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.kafkaspark.model.Limit;
import ru.kafkaspark.service.LimitService;
import ru.kafkaspark.service.PackageService;
import ru.kafkaspark.service.UpdateLimitsService;
//import ru.kafkaspark.listener.KafkaConsumer;
//import ru.kafkaspark.service.UpdateLimitsService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class TestClass implements CommandLineRunner, Serializable {

    @Autowired
    private transient KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private transient LimitService limitService;

    @Autowired
    private transient KafkaTemplate<String, Limit> limitKafkaTemplate;

    @Autowired
    private transient PackageService packageServer;

    @Autowired
    private transient UpdateLimitsService updateLimitsService;

    private static final String HOST = "localhost";
    private static final Integer PORT = 8070;
    private static int num;
    private static final String TOPIC = "alerts";

    public TestClass() {
    }

    public void startThreads() {
        new Thread(packageServer).start();
        new Thread(updateLimitsService).start();
    }

    public void startSpark() throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("VerySimpleStreamingApp");
        JavaStreamingContext streamingContext =
                new JavaStreamingContext(conf, Durations.seconds(10));
        Logger.getRootLogger().setLevel(Level.ERROR);

        // Receive streaming data from the source
        JavaReceiverInputDStream<String> lines = streamingContext.socketTextStream(HOST, PORT);

        JavaDStream<Integer> linesDStream = lines.map(new Function<String, Integer>() {
            @Override
            public Integer call(String v1) throws Exception {
                return Integer.valueOf(v1);
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
                        System.out.println("SIZE = " + collection.size());
                        num = collection.get(0);
                        System.out.println("NUM = " + num);
                        checkLimits();
                    }
                }
            }
        });
        // Execute the Spark workflow defined above
        streamingContext.start();
        streamingContext.awaitTermination();
    }

    @Override
    public void run(String... args) throws Exception {
        startThreads();
        startSpark();
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
            limitKafkaTemplate.send(TOPIC, newLimit);
        }
    }
}