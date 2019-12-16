package ru.kafkaspark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kafkaspark.listener.KafkaConsumer;
import ru.kafkaspark.model.Limit;
import java.util.Stack;

@Service
public class UpdateLimitsService implements Runnable {

    @Autowired
    private LimitService limitService;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 60 * 20);
                updateLimits();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLimits() {
        Stack<Limit> maxLimits = KafkaConsumer.getMaxLimits();
        Stack<Limit> minLimits = KafkaConsumer.getMinLimits();
        if (maxLimits.size() > 0) {
            limitService.updateLimit(KafkaConsumer.getMaxLimits().pop());
        }
        if (minLimits.size() > 0) {
            limitService.updateLimit(KafkaConsumer.getMinLimits().pop());
        }
    }
}
