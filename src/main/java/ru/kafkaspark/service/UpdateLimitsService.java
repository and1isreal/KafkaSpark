package ru.kafkaspark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kafkaspark.listener.KafkaConsumer;
import ru.kafkaspark.model.Limit;

import java.util.List;

@Service
public class UpdateLimitsService implements Runnable {

    @Autowired
    private LimitService limitService;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000);
                updateLimits();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLimits() {
        List<Limit> maxLimits = KafkaConsumer.getMaxLimits();
        List<Limit> minLimits = KafkaConsumer.getMinLimits();
        if (maxLimits.size() > 0) {
            System.out.println("UPDATING MAX");
            limitService.updateLimit(KafkaConsumer.getMaxLimits().getLast());
        }
        if (minLimits.size() > 0) {
            System.out.println("UPDATING MIN");
            limitService.updateLimit(KafkaConsumer.getMinLimits().getLast());
        }
    }
}
