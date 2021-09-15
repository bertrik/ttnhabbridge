package nl.sikken.bertrik;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.sikken.bertrik.hab.habitat.HabitatConfig;
import nl.sikken.bertrik.hab.lorawan.MqttConfig;

/**
 * Configuration interface for the application.
 */
final class TtnHabBridgeConfig {

    @JsonProperty("thethingsnetwork")
    private final MqttConfig ttnConfig = new MqttConfig("tcp://eu1.cloud.thethings.network", "appname",
            "NNSXS.SIY7VBOR2KTIDBJY7QVTILSORMGIEQ63YNDNBIY.SECRET", "v3/+/devices/+/up");

    @JsonProperty("helium")
    private final MqttConfig heliumConfig = new MqttConfig("", "user", "pass", "helium/+/rx");

    @JsonProperty("habitat")
    private final HabitatConfig habitatConfig = new HabitatConfig();

    @JsonProperty("gwCacheExpirationTime")
    private final int gwCacheExpirationTime = 600; // seconds

    @JsonProperty("payloadEncoding")
    private final String payloadEncoding = "cayenne";

    public MqttConfig getTtnConfig() {
        return ttnConfig;
    }

    public MqttConfig getHeliumConfig() {
        return heliumConfig;
    }

    public HabitatConfig getHabitatConfig() {
        return habitatConfig;
    }

    public int getGwCacheExpirationTime() {
        return gwCacheExpirationTime;
    }

    public String getPayloadEncoding() {
        return payloadEncoding;
    }

}
