//package ru.kafkaspark.app;
//
//import com.sun.jna.Platform;
//import org.apache.commons.io.output.FileWriterWithEncoding;
//import org.pcap4j.core.*;
//import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
//import org.pcap4j.packet.Packet;
//import org.pcap4j.util.NifSelector;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.kafkaspark.config.KafkaProducerConfiguration;
//import ru.kafkaspark.repository.LimitRepository;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class PcapApp implements Runnable {
//
//    private static BufferedWriter bw;
//
//    static {
//        try {
//            FileWriter writer = new FileWriter("logs.txt", true);
//            bw = new BufferedWriter(writer);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private PcapNetworkInterface device;
//
//    public PcapApp() {
//    }
//
//    public void getNetworkDevice() {
//        try {
//            device = new NifSelector().selectNetworkInterface();
////            device = new NifSelector().selectNetworkInterface();
//            if (device == null) {
//                System.out.println("No device chosen.");
//                System.exit(1);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public  void openDevice()  {
//        int snapshotLength = 65536; // in bytes
//        int readTimeout = 50; // in milliseconds
//        PcapHandle handle = null;
//        try {
//            handle = device.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
//        } catch (PcapNativeException e) {
//            e.printStackTrace();
//        }
////        final PcapDumper dumper = handle.dumpOpen("out.txt");
//
//        // Set a filter to only listen for tcp packets on port 80 (HTTP)
////        String filter = "tcp port 9999";
////        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
//
//        // Create a listener that defines what to do with the received packets
//        PacketListener listener = new PacketListener() {
//
//            public void gotPacket(Packet packet) {
//
//                try {
//                    bw.write(String.valueOf(packet.length()) + "\n");
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
////                BufferedWriter writer = null;
////                try {
////                    writer = new BufferedWriter(new FileWriter("logs.txt", true));
////                    System.out.println("packet.length() " +   packet.length());
////                    writer.write(String.valueOf(packet.length()));
////                    writer.close();
////                } catch (IOException ex) {
////                    ex.printStackTrace();
////                }
//            }
//        };
//
//        // Tell the handle to loop using the listener we created
//        try {
//            int maxPackets = -1;
//            handle.loop(maxPackets, listener);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (PcapNativeException e) {
//            e.printStackTrace();
//        } catch (NotOpenException e) {
//            e.printStackTrace();
//        }
//        // Cleanup when complete
//        handle.close();
////        dumper.close();
//    }
//
//    @Override
//    public void run() {
//        getNetworkDevice();
//        openDevice();
//    }
//}
