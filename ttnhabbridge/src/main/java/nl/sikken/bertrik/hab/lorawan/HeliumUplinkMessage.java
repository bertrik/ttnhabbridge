package nl.sikken.bertrik.hab.lorawan;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class HeliumUplinkMessage {

    @JsonProperty("app_eui")
    String appEui = "";

    @JsonProperty("fcnt")
    int fcnt;

    @JsonProperty("port")
    int port;

    @JsonProperty("name")
    String name = "";

    @JsonProperty("payload")
    byte[] payload = new byte[0];

    // milliseconds
    @JsonProperty("reported_at")
    long reportedAt;

    @JsonProperty("hotspots")
    List<HotSpot> hotSpots = new ArrayList<>();

    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class HotSpot {
        @JsonProperty("name")
        String name = "";

        @JsonProperty("lat")
        double latitude;

        @JsonProperty("long")
        double longitude;

        @JsonProperty("rssi")
        double rssi;

        @JsonProperty("snr")
        double snr;
    }

    // convert this message to the common LoRaWAN uplink message
    public LoraWanUplinkMessage toUplinkMessage() {
        LoraWanUplinkMessage uplink = new LoraWanUplinkMessage(Instant.ofEpochMilli(reportedAt), appEui, name, fcnt,
                port, payload);
        for (HotSpot hotSpot : hotSpots) {
            uplink.addGateway(hotSpot.name.trim(), hotSpot.latitude, hotSpot.longitude, Double.NaN);
        }
        return uplink;
    }

}
