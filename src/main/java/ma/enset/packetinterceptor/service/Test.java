package ma.enset.packetinterceptor.service;

import ma.enset.packetinterceptor.model.Alert;
import ma.enset.packetinterceptor.model.Packet;

import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String interfaceName = "\\Device\\NPF_{713DF5CE-9AB3-456B-A3A7-401C35DE1D93}";
        IPacketCaptureService packetCaptureService = new PacketCaptureService();
        packetCaptureService.listDevices();
        // Start capturing packets on a given interface
       /* new Thread(() -> packetCaptureService.startCapture(interfaceName)).start();

        // Simulate some delay (e.g., capturing for 10 seconds)
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop capture and retrieve packets
        List<Packet> capturedPackets = packetCaptureService.stopCapture();
        System.out.println("Captured Packets:");
        for (Packet packet : capturedPackets) {
            System.out.println(packet);
        }*/
    }
}
