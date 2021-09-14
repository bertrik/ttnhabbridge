package nl.sikken.bertrik.hab.lorawan;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.sikken.bertrik.hab.habitat.Location;

/**
 * LoRaWAN uplink message, stack independent, containing all information needed
 * to create a habhub sentence
 */
public final class LoraWanUplinkMessage {

    private final Instant time;
    private final String appId;
    private final String deviceId;
    private final int counter;
    private final int port;
    private final Map<String, Object> payloadFields = new HashMap<>();
    private final byte[] payloadRaw;
    private final List<GatewayInfo> gateways = new ArrayList<>();

    public LoraWanUplinkMessage(Instant time, String appId, String deviceId, int counter, int port, byte[] payloadRaw) {
        this.time = Instant.from(time);
        this.appId = appId;
        this.deviceId = deviceId;
        this.counter = counter;
        this.port = port;
        this.payloadRaw = payloadRaw.clone();
    }

    public void addField(String name, Object value) {
        payloadFields.put(name, value);
    }

    public Instant getTime() {
        return Instant.from(time);
    }

    public String getAppId() {
        return appId;
    }

    public String getDevId() {
        return deviceId;
    }

    public int getCounter() {
        return counter;
    }

    public byte[] getPayloadRaw() {
        return payloadRaw.clone();
    }

    public Map<String, Object> getPayloadFields() {
        return new HashMap<>(payloadFields);
    }

    public int getPort() {
        return port;
    }

    public void addGateway(String id, double lat, double lon, double alt) {
        gateways.add(new GatewayInfo(id, new Location(lat, lon, alt)));
    }

    public List<GatewayInfo> getGateways() {
        return Collections.unmodifiableList(gateways);
    }

    public static final class GatewayInfo {

        private final String id;
        private final Location location;

        public GatewayInfo(String id, Location location) {
            this.id = id;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public Location getLocation() {
            return location;
        }

    }

}
