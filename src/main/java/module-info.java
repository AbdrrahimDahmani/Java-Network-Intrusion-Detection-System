module ma.enset.packetinterceptor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.pcap4j.core;
    requires java.sql;


    opens ma.enset.packetinterceptor to javafx.fxml;
    exports ma.enset.packetinterceptor;
    exports ma.enset.packetinterceptor.controller;
    opens ma.enset.packetinterceptor.controller to javafx.fxml;
    opens ma.enset.packetinterceptor.model to javafx.fxml, javafx.base;
    opens ma.enset.packetinterceptor.service to javafx.fxml;
}