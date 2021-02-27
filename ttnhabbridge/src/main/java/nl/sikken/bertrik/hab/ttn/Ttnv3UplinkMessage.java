package nl.sikken.bertrik.hab.ttn;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of the TTNv3 uplink message.<br>
 * <br>
 * This is purely a data structure, so all fields are public for easy access.
 * All sub-structures are contained in this file too.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Ttnv3UplinkMessage {

    @JsonProperty("end_device_ids")
    EndDeviceIds endDeviceIds = new EndDeviceIds();

    @JsonProperty("received_at")
    String receivedAt = "";

    @JsonProperty("uplink_message")
    UplinkMessage uplinkMessage = new UplinkMessage();

    @JsonIgnoreProperties(ignoreUnknown = true)
    final static class EndDeviceIds {
        @JsonProperty("device_id")
        String deviceId = "";

        @JsonProperty("application_ids")
        ApplicationIds applicationIds = new ApplicationIds();

        @JsonProperty("dev_eui")
        String deviceEui = "";

        @JsonProperty("join_eui")
        String joinEui = "";

        @JsonProperty("dev_addr")
        String deviceAddress = "";
    }

    final static class ApplicationIds {
        @JsonProperty("application_id")
        String applicationId = "";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    final static class UplinkMessage {
        @JsonProperty("f_port")
        int fport = 0;

        @JsonProperty("f_cnt")
        int fcnt = 0;

        @JsonProperty("frm_payload")
        byte[] payload = new byte[0];

        @JsonProperty("rx_metadata")
        List<RxMetadata> rxMetadata = new ArrayList<>();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    final static class RxMetadata {

        @JsonProperty("gateway_ids")
        final GatewayIds gatewayIds = new GatewayIds();

        @JsonProperty("location")
        final Location location = new Location();

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    final static class GatewayIds {
        @JsonProperty("gateway_id")
        String gatewayId = "";

        @JsonProperty("eui")
        String eui = "";
    }

    final static class Location {
        @JsonProperty("latitude")
        private double latitude = Double.NaN;

        @JsonProperty("longitude")
        private double longitude = Double.NaN;

        @JsonProperty("altitude")
        private double altitude = Double.NaN;

        @JsonProperty("source")
        private String source = "";
    }

    public TtnUplinkMessage toUplinkMessage() {
        TtnUplinkMessage uplink = new TtnUplinkMessage(Instant.parse(receivedAt),
                endDeviceIds.applicationIds.applicationId, endDeviceIds.deviceId, uplinkMessage.fcnt,
                uplinkMessage.payload, false);
        for (RxMetadata metadata : uplinkMessage.rxMetadata) {
            String id = metadata.gatewayIds.gatewayId;
            if (id.isBlank()) {
                id = metadata.gatewayIds.eui;
            }
            uplink.addGateway(id, metadata.location.latitude, metadata.location.longitude, metadata.location.altitude);
        }
        return uplink;
    }

}
