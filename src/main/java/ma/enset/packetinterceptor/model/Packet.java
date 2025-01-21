package ma.enset.packetinterceptor.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Packet {
    private String sourceIP;
    private String destinationIP;
    private int size;
    private String protocol;
    private Timestamp timestamp;
    private String data;
    private String sourcePort;
    private String destinationPort;
    private Alert alert;
    public Packet() {
    }

    public Packet(String sourceIP, String destinationIP, int size, String protocol, Timestamp timestamp, String data, String sourcePort, String destinationPort) {
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.size = size;
        this.protocol = protocol;
        this.timestamp = timestamp;
        this.data = data;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getData() {
        return data;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public int getSize() {
        return size;
    }

    public String getProtocol() {
        return protocol;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "sourceIP='" + sourceIP + '\'' +
                ", destinationIP='" + destinationIP + '\'' +
                ", size=" + size +
                ", protocol='" + protocol + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                ", sourcePort='" + sourcePort + '\'' +
                ", destinationPort='" + destinationPort + '\'' +
                '}';
    }
}
