package ma.enset.packetinterceptor.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Alert {
    private String type;
    private String description;
    private String severity;
    private Timestamp timestamp;
    private String attackerIp;
    private List<Packet> packets= new ArrayList<>();
    public Alert() {
    }

    public Alert(String type, String description, String severity, Timestamp timestamp, String attackerIp) {
        this.type = type;
        this.description = description;
        this.severity = severity;
        this.timestamp = timestamp;
        this.attackerIp=attackerIp;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttackerIp() {
        return attackerIp;
    }

    public void setAttackerIp(String attackerIp) {
        this.attackerIp = attackerIp;
    }

    public List<Packet> getPackets() {
        return packets;
    }

    public void setPackets(List<Packet> packets) {
        this.packets = packets;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", severity='" + severity + '\'' +
                ", timestamp=" + timestamp +
                ", attackerIp='" + attackerIp + '\'' +
                '}';
    }
}
