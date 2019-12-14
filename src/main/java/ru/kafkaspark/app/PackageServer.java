package ru.kafkaspark.app;

import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class PackageServer implements PacketListener, Runnable{
    private static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final int PORT = 8027;
    private static PcapNetworkInterface device;
    private static BlockingQueue<String> eventQueue = new ArrayBlockingQueue<>(100);

    public static void getNetworkDevice() {
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

    @Override
    public void gotPacket(Packet packet) {
        try {
            eventQueue.put(String.valueOf(packet.length()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void openDevice()  {
        int snapshotLength = 65536; // in bytes
        int readTimeout = 50; // in milliseconds
        PcapHandle handle = null;
        try {
            handle = device.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        // Tell the handle to loop using the listener we created
        try {
            int maxPackets = -1;
            handle.loop(maxPackets, new PackageServer());
        } catch (InterruptedException | PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
        handle.close();
    }

    public static void startServer() throws IOException, InterruptedException {
        SERVER_EXECUTOR.execute(new SteamingServer());
        getNetworkDevice();
        openDevice();
    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class SteamingServer implements Runnable {
        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(PORT);
                 Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                while (true) {
                    String event = eventQueue.take();
//                    System.out.println(String.format("Writing \"%s\" to the socket.", event));
                    out.println(event);
                }
            } catch (IOException|InterruptedException e) {
                throw new RuntimeException("Server error", e);
            }
        }
    }
}
