package ru.kafkaspark.service;

import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.springframework.stereotype.Service;
import ru.kafkaspark.app.AppRunner;
import ru.kafkaspark.util.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class PackageService implements PacketListener, Runnable{

    private static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Integer PORT = 8015;
    private PcapNetworkInterface device;
    private static BlockingQueue<String> eventQueue = new ArrayBlockingQueue<>(100);
    private List<PcapNetworkInterface> pcapNetworkInterfaceList;

    public List<PcapNetworkInterface> getPcapNetworkInterfaceList() {
        return pcapNetworkInterfaceList;
    }


    public PcapNetworkInterface getDevice() {
        return device;
    }

    public void getNetworkDevice() {
        try {
            pcapNetworkInterfaceList = Pcaps.findAllDevs();
            if (pcapNetworkInterfaceList.size() < 1) {
                System.out.println("no available devices");
                System.exit(1);
            }
            device = pcapNetworkInterfaceList.get(0);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gotPacket(Packet packet) {
        checkPacket(packet);
    }

    private void checkPacket(Packet packet) {
        String srcIp;
        IpPacket ipPacket;
        if (AppRunner.getIpArg().isEmpty()) {
            try {
                eventQueue.put(String.valueOf(packet.length()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

            if (!packet.contains(IpPacket.class)) {
                return;
            }

            ipPacket = packet.get(IpPacket.class);
            srcIp = Utils.getIpFromString(ipPacket.getHeader().getSrcAddr().getHostAddress());
            System.out.println(srcIp);
            if (!srcIp.equals(AppRunner.getIpArg())) {
                try {
                    eventQueue.put(String.valueOf(packet.length()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openDevice()  {
        int snapshotLength = 65536; // in bytes
        int readTimeout = 50; // in milliseconds
        PcapHandle handle = null;
        try {
            handle = device.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        try {
            int maxPackets = -1;
            handle.loop(maxPackets, new PackageService());
        } catch (InterruptedException | PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
        handle.close();
    }

    public void startServer() throws IOException, InterruptedException {
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
                    out.println(event);
                }
            } catch (IOException|InterruptedException e) {
                throw new RuntimeException("Server error", e);
            }
        }
    }
}
