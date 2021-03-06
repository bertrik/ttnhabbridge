package nl.sikken.bertrik.hab.ttn;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a message received from the TTN MQTT stream.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Ttnv2UplinkMessage {

    @JsonProperty("app_id")
    private String appId = "";

    @JsonProperty("dev_id")
    private String devId = "";

    @JsonProperty("hardware_serial")
    private String hardwareSerial = "";

    @JsonProperty("port")
    private int port = 0;

    @JsonProperty("counter")
    private int counter = 0;

    @JsonProperty("is_retry")
    private boolean isRetry = false;

    @JsonProperty("payload_raw")
    private byte[] payloadRaw = new byte[0];

    @JsonProperty("metadata")
    private TtnMessageMetaData metaData;

    private Ttnv2UplinkMessage() {
        // Jackson constructor
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    final static class TtnMessageMetaData {

        @JsonProperty("time")
        private String time = "";

        @JsonProperty("gateways")
        private List<TtnMessageGateway> gateways = new ArrayList<>();

        private TtnMessageMetaData() {
            // empty jackson constructor
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    final static class TtnMessageGateway {

        @JsonProperty("gtw_id")
        private String id = "";

        @JsonProperty("latitude")
        private double latitude = Double.NaN;

        @JsonProperty("longitude")
        private double longitude = Double.NaN;

        @JsonProperty("altitude")
        private double altitude = Double.NaN;
    }

    public TtnUplinkMessage toUplinkMessage() {
        TtnUplinkMessage message = new TtnUplinkMessage(Instant.parse(metaData.time), appId, devId, counter, port,
                payloadRaw, isRetry);
        for (TtnMessageGateway gw : metaData.gateways) {
            message.addGateway(gw.id, gw.latitude, gw.longitude, gw.altitude);
        }
        return message;
    }

}
