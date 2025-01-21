package ma.enset.packetinterceptor.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;
import ma.enset.packetinterceptor.helper.DatabaseHelper;
import ma.enset.packetinterceptor.model.Alert;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticController implements Initializable {

    @FXML
    private BarChart<String, Number> barChartAlert;

    @FXML
    private CategoryAxis barChartX;

    @FXML
    private NumberAxis barChartY;

    @FXML
    private PieChart pieChart;

    @FXML
    private Text textNumPacket;

    @FXML
    private Text textNumAlert;

    private List<Alert> alertList;
    private int numCapturedPackets;

    public void fetchStatisticsData() {

        this.alertList=DatabaseHelper.getAlerts();
        this.numCapturedPackets = DatabaseHelper.getTotalCapturedTraffic();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchStatisticsData();
        // Initialize statistics
        initBarChart();
        initPieChart();
        updateTextStatistics();
    }

    private void initBarChart() {
        if (alertList != null && !alertList.isEmpty()) {
            // Clear previous data
            barChartAlert.getData().clear();

            // Example: Count alerts by severity and display them in the bar chart
            long highSeverityCount = alertList.stream()
                    .filter(alert -> "High".equalsIgnoreCase(alert.getSeverity()))
                    .count();
            long mediumSeverityCount = alertList.stream()
                    .filter(alert -> "Medium".equalsIgnoreCase(alert.getSeverity()))
                    .count();
            long lowSeverityCount = alertList.stream()
                    .filter(alert -> "Low".equalsIgnoreCase(alert.getSeverity()))
                    .count();

            // Add data to the bar chart
            var series = new BarChart.Series<String, Number>();
            series.setName("Alerts by Severity");
            series.getData().add(new BarChart.Data<>("High", highSeverityCount));
            series.getData().add(new BarChart.Data<>("Medium", mediumSeverityCount));
            series.getData().add(new BarChart.Data<>("Low", lowSeverityCount));

            barChartAlert.getData().add(series);
        }
    }

    private void initPieChart() {
        if (alertList != null && !alertList.isEmpty()) {
            // Clear previous data
            pieChart.getData().clear();

            // Example: Count alerts by type and display them in the pie chart
            long portScanCount = alertList.stream()
                    .filter(alert -> "Port Scan".equalsIgnoreCase(alert.getType()))
                    .count();
            long dosAttackCount = alertList.stream()
                    .filter(alert -> "DoS Attack".equalsIgnoreCase(alert.getType()))
                    .count();
            long suspiciousTrafficCount = alertList.stream()
                    .filter(alert -> "Suspicious Traffic".equalsIgnoreCase(alert.getType()))
                    .count();

            // Add data to the pie chart
            pieChart.getData().add(new PieChart.Data("Port Scan", portScanCount));
            pieChart.getData().add(new PieChart.Data("DoS Attack", dosAttackCount));
            pieChart.getData().add(new PieChart.Data("Suspicious Traffic", suspiciousTrafficCount));
        }
    }

    private void updateTextStatistics() {
        // Update the "Number of Packets Captured" text
        textNumPacket.setText(String.valueOf(numCapturedPackets));

        // Update the "Number of Alerts" text
        if (alertList != null) {
            textNumAlert.setText(String.valueOf(alertList.size()));
        } else {
            textNumAlert.setText("0");
        }
    }

}
