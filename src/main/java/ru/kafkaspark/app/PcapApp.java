package ru.kafkaspark.app;

import com.sun.jna.Platform;
import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kafkaspark.config.KafkaProducerConfiguration;
import ru.kafkaspark.repository.LimitRepository;

import java.io.IOException;

@Component
public class PcapApp {

    private PcapNetworkInterface device;

    public PcapApp() {
    }

    public void getNetworkDevice() {
        try {
            device = new NifSelector().selectNetworkInterface();
            if (device == null) {
                System.out.println("No device chosen.");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void openDevice() throws PcapNativeException, NotOpenException {
        int snapshotLength = 65536; // in bytes
        int readTimeout = 50; // in milliseconds
        final PcapHandle handle;
        handle = device.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
        final PcapDumper dumper = handle.dumpOpen("out.pcap");

        // Set a filter to only listen for tcp packets on port 80 (HTTP)
        String filter = "tcp port 9999";
        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);

        // Create a listener that defines what to do with the received packets
        PacketListener listener = new PacketListener() {

            int sum = 0;
            public void gotPacket(Packet packet) {
                // Print packet information to screen
//                System.out.println(handle.getTimestamp());
//                System.out.println(packet);
//                sum += packet.length();
//                System.out.println(sum);

                // Dump packets to file
                try {
                    dumper.dump(packet, handle.getTimestamp());
                } catch (NotOpenException e) {
                    e.printStackTrace();
                }
            }
        };

        // Tell the handle to loop using the listener we created
        try {
            int maxPackets = -1;
            handle.loop(maxPackets, listener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print out handle statistics
        PcapStat stats = handle.getStats();
        System.out.println("Packets received: " + stats.getNumPacketsReceived());
        System.out.println("Packets dropped: " + stats.getNumPacketsDropped());
        System.out.println("Packets dropped by interface: " + stats.getNumPacketsDroppedByIf());
        // Supported by WinPcap only
//        if (Platform.isWindows()) {
        if (Platform.isLinux()) {
            System.out.println("Packets captured: " +stats.getNumPacketsCaptured());
        }

        // Cleanup when complete
        dumper.close();
        handle.close();
    }
}
