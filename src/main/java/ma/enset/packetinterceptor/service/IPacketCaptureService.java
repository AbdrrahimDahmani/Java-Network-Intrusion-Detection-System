package ma.enset.packetinterceptor.service;

import ma.enset.packetinterceptor.model.Packet;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;

import java.util.List;

public interface IPacketCaptureService {
    public void startCapture(String interfaceName);

    // Stops the ongoing packet capture.
    public List<Packet> stopCapture();

    // Processes an individual captured packet.
    public void processPacket(Packet packet);

    // Retrieves the list of all captured packets.
    public List<Packet> getCapturedPackets();

    public Packet adaptPacket(org.pcap4j.packet.Packet pcapPacket, PcapHandle handle);

    public List<String> listDevices();
    public boolean isCapturing();

}
