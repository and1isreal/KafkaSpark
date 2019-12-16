package ru.kafkaspark.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kafkaspark.service.PackageService;
import ru.kafkaspark.service.UpdateLimitsService;
import ru.kafkaspark.spark.SparkStream;
import ru.kafkaspark.util.Utils;

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    private SparkStream sparkStream;

    @Autowired
    private PackageService packageServer;

    @Autowired
    private UpdateLimitsService updateLimitsService;

    private static String ipArg = "";

    public AppRunner() {
    }

    public void startThreads() throws InterruptedException {
        new Thread(packageServer).start();
        new Thread(updateLimitsService).start();
        sparkStream.startSpark();
    }
    @Override
    public void run(String... args) throws Exception {
        if (args.length == 1 && Utils.isParameterValid(args[0])) {
            ipArg = args[0];
        }
        startThreads();
    }

    public static String getIpArg() {
        return ipArg;
    }
}