//package ru.kafkaspark.app;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.springframework.boot.CommandLineRunner;
//
//public class SparkReadFileTest implements CommandLineRunner {
//    @Override
//    public void run(String... args) throws Exception {
//        SparkConf sparkConf = new SparkConf()
//                .setAppName("Example Spark App")
//                .setMaster("local[*]");  // Delete this line when submitting to a cluster
//
//        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
//
//        JavaRDD<String> stringJavaRDD = sparkContext.textFile("out.txt");
//
//        System.out.println("Number of lines in file = " + stringJavaRDD.count());
//    }
//}
