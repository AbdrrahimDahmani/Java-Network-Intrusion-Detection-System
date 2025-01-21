package ma.enset.packetinterceptor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ma.enset.packetinterceptor.InterceptorApplication;
import ma.enset.packetinterceptor.helper.DatabaseHelper;
import ma.enset.packetinterceptor.model.Alert;
import ma.enset.packetinterceptor.model.Packet;
import ma.enset.packetinterceptor.model.SharedData;
import ma.enset.packetinterceptor.service.IIntrusionDetectionService;
import ma.enset.packetinterceptor.service.IntrusionDetectionService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AlertController implements Initializable {
    private List<Packet> capturedPackets=new ArrayList<>();

    public void setCapturedPackets(List<Packet> capturedPackets) {
        this.capturedPackets = capturedPackets;
    }

    @FXML
    private TableView<Alert> tableAlert;
    @FXML
    private TableColumn<Alert, String> columnType;
    @FXML
    private TableColumn<Alert, String> columnAttackerIp; // Assume this maps to a relevant field in Alert
    @FXML
    private TableColumn<Alert, String> columnDescription;
    @FXML
    private TableColumn<Alert, String> columnSeverity;
    @FXML
    private TableColumn<Alert, String> columnTimestamp;

    private IIntrusionDetectionService intrusionDetectionService;
    private ObservableList<Alert> alertList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Packet> capturedTrafic=SharedData.getInstance().getPacketList();

        if(!capturedTrafic.isEmpty()){

            DatabaseHelper.createAlertsTable();
            // Initialize the intrusion detection service
            intrusionDetectionService = new IntrusionDetectionService();

            // Detecting sus trafic
            for (Packet packet : capturedTrafic) {
                System.out.println(packet.getData());
                intrusionDetectionService.detectPortScan(packet);
                intrusionDetectionService.detectDoSAttack(packet);
                intrusionDetectionService.detectSuspiciousTraffic(packet);
            }

            // Bind columns to Alert model properties
            columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
            columnAttackerIp.setCellValueFactory(new PropertyValueFactory<>("attackerIp")); // Ensure Alert has this field or remove
            columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            columnSeverity.setCellValueFactory(new PropertyValueFactory<>("severity"));
            columnTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            // load alerts
            loadAlerts();
            // Populate the TableView with alerts
            tableAlert.setItems(alertList);
        }
    }

    private void loadAlerts() {
        // Fetch alerts from the IntrusionDetectionService
        List<Alert> alerts=intrusionDetectionService.getAllAlerts();
        alertList.addAll(alerts);
        for(Alert alert : alerts){
            DatabaseHelper.insertAlert(alert);
        }
    }
    @FXML
    private void openStatistics(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(InterceptorApplication.class.getResource("statistic-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 760, 400);
            stage.setTitle("Intrusion Detection System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
