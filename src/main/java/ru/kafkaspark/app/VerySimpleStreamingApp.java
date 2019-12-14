//package ru.kafkaspark.app;
//
//import org.apache.log4j.*;
//import org.apache.spark.SparkConf;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.api.java.*;
//import org.pcap4j.core.NotOpenException;
//import org.pcap4j.core.PcapNativeException;
//import org.pcap4j.packet.Packet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import ru.kafkaspark.config.KafkaProducerConfiguration;
//import ru.kafkaspark.repository.LimitRepository;
//
//@Component
//public class VerySimpleStreamingApp implements CommandLineRunner {
//
//    @Autowired
//    private PcapApp pcapApp;
//
//    @Autowired
//    private LimitRepository limitRepository;
//
//    @Autowired
//    private KafkaProducerConfiguration producer;
//
//    private static final String HOST = "localhost";
//
//    private static final int PORT = 9999;
//
//    @Override
//    public void run(String... args) throws Exception {
////        SparkConf conf = new SparkConf()
////                .setMaster("local[*]")
////                .setAppName("VerySimpleStreamingApp");
////
////        JavaStreamingContext streamingContext =
////                new JavaStreamingContext(conf, Durations.seconds(5));
////
////
////        Logger.getRootLogger().setLevel(Level.ERROR);
//
//        pcapApp.getNetworkDevice();
////        pcapApp.openDevice();
//
//
//        // Receive streaming data from the source
////        JavaReceiverInputDStream<String> lines = streamingContext.socketTextStream(HOST, PORT);
////        JavaReceiverInputDStream<Packet> packets = streamingContext.rawSocketStream(HOST, PORT);
//
//
////        pcapApp.getNetworkDevice();
//        pcapApp.openDevice();
//
////        packets.print();
////
////        // Execute the Spark workflow defined above
////        streamingContext.start();
////        streamingContext.awaitTermination();
//    }
//}