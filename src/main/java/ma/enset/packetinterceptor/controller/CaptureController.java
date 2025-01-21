package ma.enset.packetinterceptor.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ma.enset.packetinterceptor.InterceptorApplication;
import ma.enset.packetinterceptor.helper.DatabaseHelper;
import ma.enset.packetinterceptor.model.Packet;
import ma.enset.packetinterceptor.model.SharedData;
import ma.enset.packetinterceptor.service.IPacketCaptureService;
import ma.enset.packetinterceptor.service.PacketCaptureService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CaptureController implements Initializable {
    @FXML
    private ComboBox<String> comboFInterface;
    @FXML
    private TableView<Packet> tableCapture;
    @FXML
    private TableColumn<Packet, String> columnSourceIp;
    @FXML
    private TableColumn<Packet, String> columnDestIp;
    @FXML
    private TableColumn<Packet, String> columnDestPort;
    @FXML
    private TableColumn<Packet, String> columnProtocol;
    @FXML
    private TableColumn<Packet, Date> columnTime;
    @FXML
    private TableColumn<Packet, String> columnSize;
    @FXML
    private TableColumn<Packet,String> columnPayload;
    List<Packet> finalPackets;
    private IPacketCaptureService packetCaptureService;
    private ObservableList<Packet> packetList = FXCollections.observableArrayList();
    private Thread captureThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create the statistics table in the database
        DatabaseHelper.createStatisticsTable();

        // Initialize packet capture service
        packetCaptureService = new PacketCaptureService();

        // Bind table columns to Packet model fields
        columnSourceIp.setCellValueFactory(new PropertyValueFactory<>("sourceIP"));
        columnDestIp.setCellValueFactory(new PropertyValueFactory<>("destinationIP"));
        columnDestPort.setCellValueFactory(new PropertyValueFactory<>("destinationPort"));
        columnProtocol.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        columnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        columnPayload.setCellValueFactory(new PropertyValueFactory<>("data"));
        // Bind observable list to table view
        tableCapture.setItems(packetList);

        // Populate combo box with available interfaces
        populateInterfaceComboBox();
    }


    private void populateInterfaceComboBox() {
        List<String> devices = packetCaptureService.listDevices(); // Fetch device names
        if (devices.isEmpty()) {
            comboFInterface.setPromptText("No devices found");
        } else {
            comboFInterface.getItems().addAll(devices);
            comboFInterface.setPromptText("Select an interface");
        }
    }

    @FXML
    private void startCapture() {
        // Clear any existing data
        packetList.clear();

        String selectedDevice = comboFInterface.getValue(); // Get selected interface
        if (selectedDevice == null || selectedDevice.isEmpty()) {
            System.err.println("Interface selection is required!");
            return;
        }

        // Extract interface name (everything before the delimiter "-")
        String interfaceName = selectedDevice.split(" - ")[0].trim();
        System.out.println("Selected interface: " + interfaceName);

        // Start packet capture in a new thread
        captureThread = new Thread(() -> {
            try {
                packetCaptureService.startCapture(interfaceName);

                while (true) {
                    // Continuously fetch captured packets
                    List<Packet> capturedPackets = packetCaptureService.getCapturedPackets();

                    // Update UI on JavaFX Application Thread
                    Platform.runLater(() -> {
                        packetList.clear();
                        packetList.addAll(capturedPackets);
                    });

                    // Sleep briefly to reduce CPU usage
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.err.println("Error during packet capture: " + e.getMessage());
            }
        });

        captureThread.setDaemon(true); // Allow thread to terminate when application closes
        captureThread.start();
    }

    @FXML
    private void stopCapture() {
        if (captureThread != null && captureThread.isAlive()) {
            try {
                // Stop the capture and update the UI
                finalPackets = packetCaptureService.stopCapture();

                // Update table view with final list of packets
                Platform.runLater(() -> {
                    packetList.clear();
                    packetList.addAll(finalPackets);
                    DatabaseHelper.insertStatistic(finalPackets.size());
                });

                // Interrupt the thread
                captureThread.interrupt();
                System.out.println("Packet capture stopped.");

            } catch (Exception e) {
                System.err.println("Error stopping capture: " + e.getMessage());
            }
        }
    }
    @FXML
    private void openAlertView() {
        try {
            SharedData.getInstance().getPacketList().addAll(finalPackets);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(InterceptorApplication.class.getResource("alert-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 760, 400);
            stage.setTitle("Intrusion Detection System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
