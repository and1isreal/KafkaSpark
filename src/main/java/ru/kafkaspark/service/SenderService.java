package ru.kafkaspark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.kafkaspark.model.Limit;

@Service
public class SenderService {

    @Autowired
    private KafkaTemplate<String, Limit> limitKafkaTemplate;

    public void sendJSON(String topic, Limit limit) {
        limitKafkaTemplate.send(topic, limit);
    }

}
