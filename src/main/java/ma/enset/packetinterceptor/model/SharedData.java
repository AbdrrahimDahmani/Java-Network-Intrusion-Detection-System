package ma.enset.packetinterceptor.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedData {
    private static SharedData instance = new SharedData();

    private ObservableList<Packet> packetList = FXCollections.observableArrayList();

    private SharedData() {}

    public static SharedData getInstance() {
        return instance;
    }

    public ObservableList<Packet> getPacketList() {
        return packetList;
    }
}

