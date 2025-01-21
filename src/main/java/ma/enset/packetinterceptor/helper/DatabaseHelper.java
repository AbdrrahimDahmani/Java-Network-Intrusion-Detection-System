package ma.enset.packetinterceptor.helper;

import ma.enset.packetinterceptor.model.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:alerts.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createAlertsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS alerts ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type TEXT,"
                + "attackerIp TEXT,"
                + "description TEXT,"
                + "severity TEXT,"
                + "timestamp DATETIME)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createStatisticsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS statistics ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "numCapturedTraffic INTEGER, "
                + "timestamp DATETIME)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertStatistic(int numCaptured) {
        String sql = "INSERT INTO statistics(numCapturedTraffic, timestamp) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numCaptured);


            java.util.Date now = new java.util.Date();
            stmt.setTimestamp(2, new java.sql.Timestamp(now.getTime()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTotalCapturedTraffic() {
        String sql = "SELECT SUM(numCapturedTraffic) AS total FROM statistics";
        int totalCapturedTraffic = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalCapturedTraffic = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCapturedTraffic;
    }


    public static void insertAlert(Alert alert) {
        String sql = "INSERT INTO alerts(type, attackerIp, description, severity, timestamp) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, alert.getType());
            stmt.setString(2, alert.getAttackerIp());
            stmt.setString(3, alert.getDescription());
            stmt.setString(4, alert.getSeverity());
            stmt.setTimestamp(5, alert.getTimestamp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Alert> getAlerts() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM alerts";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Loop through the result set and create Alert objects
            while (rs.next()) {
                Alert alert = new Alert();
                alert.setType(rs.getString("type"));
                alert.setAttackerIp(rs.getString("attackerIp")); // Replace with the correct column name
                alert.setDescription(rs.getString("description"));
                alert.setSeverity(rs.getString("severity"));
                alert.setTimestamp(rs.getTimestamp("timestamp"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alerts;
    }

}
