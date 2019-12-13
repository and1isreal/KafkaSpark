//package ru.kafkaspark.listener;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import ru.kafkaspark.model.Limit;
//
//@Service
//public class KafkaConsumer {
//
////    @KafkaListener(topics = "alerts", groupId = "group_id")
////    public void consume(String message) {
////        System.out.println("consumed msg -> " + message);
////    }
//
//    @KafkaListener(topics = "alerts", groupId = "group_json", containerFactory = "userConcurrentKafkaListenerContainerFactory")
//    public void consumeJson(Limit user) {
//        System.out.println("consumed JSON msg -> " + user);
//    }
//}
