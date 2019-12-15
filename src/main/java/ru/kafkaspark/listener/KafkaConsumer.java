package ru.kafkaspark.listener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.kafkaspark.model.Limit;
import java.util.LinkedList;


@Service
public class KafkaConsumer{

    private static LinkedList<Limit> minLimits = new LinkedList<>();
    private static LinkedList<Limit> maxLimits = new LinkedList<>();

    @KafkaListener(topics = "alerts", groupId = "group_json", containerFactory = "limitConcurrentKafkaListenerContainerFactory")
    public void consumeJson(Limit limit) {
        System.out.println("consumed JSON msg -> " + limit);
        if (limit.getName().equals("min"))
            minLimits.add(limit);
        else if (limit.getName().equals("max"))
            maxLimits.add(limit);
    }

    public static LinkedList<Limit> getMinLimits() {
        return minLimits;
    }

    public static LinkedList<Limit> getMaxLimits() {
        return maxLimits;
    }
}
