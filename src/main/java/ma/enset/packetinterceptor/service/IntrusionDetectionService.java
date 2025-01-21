package ma.enset.packetinterceptor.service;

import ma.enset.packetinterceptor.model.Alert;
import ma.enset.packetinterceptor.model.Packet;

import java.sql.Timestamp;
import java.util.*;

public class IntrusionDetectionService implements IIntrusionDetectionService{
    Map<String, String> susPort = new HashMap<>();
    private List<Alert> alerts = new ArrayList<>();
    private final Map<String, List<Long>> packetHistory = new HashMap<>();
    private static final int DOS_THRESHOLD = 100; // Number of packets
    private static final long TIME_WINDOW_MS = 1000; // Time window in milliseconds
    private List<Packet> susPackets = new ArrayList<>();
    public IntrusionDetectionService() {
        susPort.put("137", "NetBIOS over TCP");
        susPort.put("139", "NetBIOS over TCP");
        susPort.put("445", "SMB");
        susPort.put("22", "SSH");
        susPort.put("53", "DNS");
        susPort.put("25", "SMTP");
        susPort.put("3389", "Remote Desktop");
        susPort.put("80", "HTTP");
        susPort.put("1433", "Microsoft SQL Server 2022");
        susPort.put("903", "VMware Authentication");
        susPort.put("20", "FTP");
        susPort.put("21", "FTP");
        susPort.put("23", "Telnet");
        susPort.put("1433", "Database");
        susPort.put("1434", "Database");
        susPort.put("3306", "Database");
    }

    @Override
    public void detectPortScan(Packet packet) {
        String attackerIp=packet.getDestinationIP();
        System.out.println("attacker Ip: "+ attackerIp);
        if(packet.getDestinationPort()!=null && attackerIp!=null && !attackerIp.trim().isEmpty()){
            if (susPort.get(packet.getDestinationPort()) != null) {
                System.out.println("Potential Suspicious Activity detected on port " + packet.getDestinationPort() + " about " + susPort.get(packet.getDestinationPort()));
                Alert alert = generateAlert("Port Scan", "Potential suspicious ort scan activity detected on port " + packet.getDestinationPort() + " about " + susPort.get(packet.getDestinationPort()), "Medium", packet.getTimestamp(), attackerIp);
                alerts.add(alert);
                addSusPacket(packet);
            }
        }

    }

    @Override
    public boolean detectDoSAttack(Packet packet) {
        String attackerIp = packet.getDestinationIP();
        if(packet.getDestinationPort()!=null && attackerIp!=null && !attackerIp.trim().isEmpty()){

            long timestamp = packet.getTimestamp().getTime();

            // Add packet timestamp to the history
            packetHistory.putIfAbsent(attackerIp, new ArrayList<>());
            List<Long> timestamps = packetHistory.get(attackerIp);
            timestamps.add(timestamp);

            // Remove outdated entries
            timestamps.removeIf(time -> time < timestamp - TIME_WINDOW_MS);

            // Check if the number of packets exceeds the threshold
            if (timestamps.size() > DOS_THRESHOLD) {
                System.out.println("DoS attack detected on IP: " + attackerIp);
                Alert alert = generateAlert("DoS Attack", "Potential Denial of Service (DoS) attack detected on IP: " + attackerIp, "High", packet.getTimestamp(), attackerIp);
                alerts.add(alert);
                return true;
            }
        }
        return false;
    }

    @Override
    public void detectSuspiciousTraffic(Packet packet) {
        String attackerIp = packet.getDestinationIP();
        if(packet.getDestinationPort()!=null && attackerIp!=null && !attackerIp.trim().isEmpty()){
            if (packet.getDestinationPort().equals("80") || packet.getDestinationPort().equals("443")) {
                if (packet.getSize() > 1000) {
                    System.out.println("Potential Suspicious Traffic detected on IP: " + attackerIp);
                    Alert alert = generateAlert("Suspicious Traffic", "Potential suspicious traffic detected on IP: " + attackerIp, "Low", packet.getTimestamp(), attackerIp);
                    alerts.add(alert);
                    addSusPacket(packet);
                }
            }
        }

    }

    @Override
    public Alert generateAlert(String type, String details, String Severity, Timestamp timestamp, String attackerIp) {
        return new Alert(type, details,Severity,timestamp,attackerIp);
    }

    @Override
    public List<Alert> getAllAlerts() {
        return alerts;
    }
    public void addSusPacket(Packet packet){
        susPackets.add(packet);
    }
    public List<Packet> getSusPackets() {
        return susPackets;
    }
}
