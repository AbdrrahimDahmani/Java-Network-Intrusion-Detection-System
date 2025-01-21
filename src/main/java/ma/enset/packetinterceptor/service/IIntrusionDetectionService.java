package ma.enset.packetinterceptor.service;

import javafx.collections.ObservableList;
import ma.enset.packetinterceptor.model.Alert;
import ma.enset.packetinterceptor.model.Packet;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface IIntrusionDetectionService {
    // Detects potential port scanning attacks in the provided packet list.
    public void detectPortScan(Packet packet);

    // Detects potential DoS (Denial of Service) attacks in the provided packet list.
    public boolean detectDoSAttack(Packet packet);

    // Detects suspicious traffic based on a heuristic or rule-based approach.
    public void detectSuspiciousTraffic(Packet packet);

    // Generates an alert for the detected intrusion.
    public Alert generateAlert(String type, String details, String Severity, Timestamp timestamp, String attackerIp);

    // Retrieves a list of all generated alerts.
    public List<Alert> getAllAlerts();

}
