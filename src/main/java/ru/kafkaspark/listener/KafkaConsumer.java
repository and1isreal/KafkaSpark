package ru.kafkaspark.listener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.kafkaspark.model.Limit;
import java.util.Stack;


@Service
public class KafkaConsumer{

    private static Stack<Limit> minLimits = new Stack<>();
    private static  Stack<Limit> maxLimits = new Stack<>();

    @KafkaListener(topics = "alerts", groupId = "group_json", containerFactory = "limitConcurrentKafkaListenerContainerFactory")
    public void consumeJson(Limit limit) {
        if (limit.getName().equals("min"))
            minLimits.push(limit);
        else if (limit.getName().equals("max"))
            maxLimits.push(limit);
    }

    public static Stack<Limit> getMinLimits() {
        return minLimits;
    }

    public static Stack<Limit> getMaxLimits() {
        return maxLimits;
    }
}
