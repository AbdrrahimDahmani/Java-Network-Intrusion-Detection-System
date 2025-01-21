package ma.enset.packetinterceptor.service;

import ma.enset.packetinterceptor.model.Packet;
import org.pcap4j.core.*;

import java.io.EOFException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketCaptureService implements IPacketCaptureService{
    private PcapHandle handle;
    private boolean capturing=true;
    private List<Packet> capturedPackets = new ArrayList<>();
    @Override
    public void startCapture(String interfaceName) {
        try {
            PcapNetworkInterface nif = Pcaps.getDevByName(interfaceName);
            int snapLen = 65536;
            int readTimeout = 1000;
            handle = nif.openLive(snapLen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, readTimeout);
            capturing = true;

            while (capturing) {
                try {
                    org.pcap4j.packet.Packet packet = handle.getNextPacketEx();
                    if (packet != null) {
                        Packet adaptedPacket = adaptPacket(packet, handle);
                        processPacket(adaptedPacket);
                    }
                    if (handle == null) {
                        break;
                    }
                } catch (java.util.concurrent.TimeoutException te) {
                    // Timeout occurred, continue listening
                    System.err.println("Timeout: No packet captured within the timeout period.");
                } catch (EOFException eof) {
                    System.err.println("EOFException: Packet capture stream ended unexpectedly.");
                    capturing = false;
                    break; // Exit the capture loop
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Packet> stopCapture() {
        capturing = false;

        if (handle != null) {
            System.out.println("Stopping packet capture...");
            try {
                handle.breakLoop();
                handle.close();
            } catch (NotOpenException e) {
                throw new RuntimeException(e);
            }
        }

        return capturedPackets; // Return the list of captured packets
    }


    @Override
    public void processPacket(Packet packet) {
        capturedPackets.add(packet);
    }

    @Override
    public List<Packet> getCapturedPackets() {
        return capturedPackets;
    }
    @Override
    public Packet adaptPacket(org.pcap4j.packet.Packet pcapPacket, PcapHandle handle) {
        Packet modelPacket = new Packet();
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Set the timestamp using the PcapHandle
        try {
            Date utilDate=dateFormat.parse(dateFormat.format(handle.getTimestamp()));
            modelPacket.setTimestamp(new java.sql.Timestamp(utilDate.getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        modelPacket.setSize(pcapPacket.length());

        // Extract protocol, source IP, destination IP, and ports
        if (pcapPacket.contains(org.pcap4j.packet.IpV4Packet.class)) {
            org.pcap4j.packet.IpV4Packet ipv4Packet = pcapPacket.get(org.pcap4j.packet.IpV4Packet.class);
            modelPacket.setSourceIP(ipv4Packet.getHeader().getSrcAddr().getHostAddress());
            modelPacket.setDestinationIP(ipv4Packet.getHeader().getDstAddr().getHostAddress());

            // Check for transport layer protocols (TCP/UDP)
            if (ipv4Packet.contains(org.pcap4j.packet.TcpPacket.class)) {
                org.pcap4j.packet.TcpPacket tcpPacket = ipv4Packet.get(org.pcap4j.packet.TcpPacket.class);
                modelPacket.setProtocol("TCP");
                modelPacket.setSourcePort(String.valueOf(tcpPacket.getHeader().getSrcPort().valueAsInt()));
                modelPacket.setDestinationPort(String.valueOf(tcpPacket.getHeader().getDstPort().valueAsInt()));
            } else if (ipv4Packet.contains(org.pcap4j.packet.UdpPacket.class)) {
                org.pcap4j.packet.UdpPacket udpPacket = ipv4Packet.get(org.pcap4j.packet.UdpPacket.class);
                modelPacket.setProtocol("UDP");
                modelPacket.setSourcePort(String.valueOf(udpPacket.getHeader().getSrcPort().valueAsInt()));
                modelPacket.setDestinationPort(String.valueOf(udpPacket.getHeader().getDstPort().valueAsInt()));
            }
        }

        // Extract payload data (if available)
        if (pcapPacket.getPayload() != null) {
            modelPacket.setData(decodeHexToString(pcapPacket.getPayload().toString()));
        } else {
            modelPacket.setData("No Payload");
        }

        return modelPacket;
    }

    @Override
    public List<String> listDevices() {
        List<String> deviceNames = new ArrayList<>();
        try {
            List<PcapNetworkInterface> devices = Pcaps.findAllDevs();
            if (devices.isEmpty()) {
                System.out.println("No devices found.");
            } else {
                System.out.println("Available devices:");
                for (PcapNetworkInterface device : devices) {
                    String deviceInfo = device.getName() + " - " + (device.getDescription() != null ? device.getDescription() : "No description");
                    deviceNames.add(deviceInfo);
                    System.out.println(deviceInfo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return deviceNames;
    }

    @Override
    public boolean isCapturing() {
        return capturing;
    }

    private String decodeHexToString(String hexStream) {
        if (hexStream == null || hexStream.isEmpty()) {
            return "";
        }

        StringBuilder decoded = new StringBuilder();
        String[] hexBytes = hexStream.split("\\s+"); // Split hex by spaces

        for (String hexByte : hexBytes) {
            try {
                int charCode = Integer.parseInt(hexByte, 16);
                if (charCode >= 32 && charCode <= 126) { // Only include printable ASCII characters
                    decoded.append((char) charCode);
                } else {
                    decoded.append('.'); // Non-printable characters as "."
                }
            } catch (NumberFormatException e) {
                decoded.append('.'); // Invalid hex byte
            }
        }

        return decoded.toString();
    }

}
