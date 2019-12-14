package ru.kafkaspark.app;

import org.apache.log4j.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kafkaspark.model.Limit;
import ru.kafkaspark.repository.LimitRepository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
public class TestClass implements CommandLineRunner, Serializable {

//    @Autowired
//    private PcapApp pcapApp;
//
    @Autowired
    private LimitRepository limitRepository;
//
//    @Autowired
//    private KafkaProducerConfiguration producer;


    private static final String HOST = "localhost";

    private static final int PORT = 8027;

    private static int[] array = new int[1];

    @Override
    public void run(String... args) throws Exception {

        new Thread(new PackageServer()).start();
        Thread.sleep(5000);

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("VerySimpleStreamingApp");
        JavaStreamingContext streamingContext =
                new JavaStreamingContext(conf, Durations.seconds(5));
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
                    System.out.println("inside IF");
                    array[0] = integerJavaRDD.collect().get(0);
                    System.out.println("array[0] = " + Arrays.toString(array));
                    checkLimits();
                }
            }
        });
        // Execute the Spark workflow defined above
        streamingContext.start();
        streamingContext.awaitTermination();
    }

    public void checkLimits() {
        Limit min = limitRepository.findLimitByName("min");
        Limit max = limitRepository.findLimitByName("max");

        if (array[0] < min.getValue()) {
            System.out.println("INSIDE checkLimits() IF");
            min.setValue(array[0]);
            limitRepository.save(min);
        } else if (array[0] > max.getValue()) {
            System.out.println("INSIDE checkLimits() ELSE");
            min.setValue(array[0]);
            limitRepository.save(max);
        }
    }
}