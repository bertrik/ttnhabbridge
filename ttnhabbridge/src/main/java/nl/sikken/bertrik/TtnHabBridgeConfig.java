package nl.sikken.bertrik;

import java.time.Duration;

/**
 * Configuration class.
 */
final class TtnHabBridgeConfig extends BaseConfig implements ITtnHabBridgeConfig {
    
    /**
     * One enumeration item per configuration item.
     */
    private enum EConfigItem {
        HABITAT_URL("habitat.url", "http://habitat.habhub.org", "URL of the habitat server"),
        HABITAT_TIMEOUT_MS("habitat.timeout", "5000", "Timeout in milliseconds"),

        TTN_MQTT_URL("ttn.mqtt.url", "tcp://eu.thethings.network", "URL of the TTN MQTT server"),
        TTN_APP_ID("ttn.app.id", "habhub", "TTN Application Id (e.g. habhub, ttnmapper, etc.)"),
        TTN_APP_KEY("ttn.app.key", "ttn-account-v2.Sh49WL90oQz-ZuxoDrS6yKuACL_jtAA0agdDfO_eVj4", "TTN Application key"),
        TTN_GW_CACHE_EXPIRY_SEC("ttn.gwcache.expiry", "600", "Gateway cache expiration time (seconds)"),
        TTN_PAYLOAD_ENCODING("ttn.payload.encoding", "cayenne",
                "Payload format, allowed values: 'sodaqone','json','cayenne','custom_format_icss'"),
        ;
        
        private final String key;
        private final String value;
        private final String comment;

        EConfigItem(String key, String def, String comment) {
            this.key = key;
            this.value = def;
            this.comment = comment;
        }
    }
    
    /**
     * Constructor.
     * 
     * Configures all settings to their default value.
     */
    public TtnHabBridgeConfig() {
        for (EConfigItem e : EConfigItem.values()) {
            add(e.key, e.value, e.comment);
        }
    }
    
    @Override
    public Duration getHabitatTimeout() {
        return Duration.ofMillis(Integer.parseInt(get(EConfigItem.HABITAT_TIMEOUT_MS.key)));
    }

    @Override
    public String getHabitatUrl() {
        return get(EConfigItem.HABITAT_URL.key);
    }

    @Override
    public String getTtnMqttUrl() {
        return get(EConfigItem.TTN_MQTT_URL.key);
    }

    @Override 
    public String getTtnAppId() {
        return get(EConfigItem.TTN_APP_ID.key);
    }
    
    @Override 
    public String getTtnAppKey() {
        return get(EConfigItem.TTN_APP_KEY.key);
    }

    @Override
    public Duration getTtnGwCacheExpiry() {
        return Duration.ofSeconds(Integer.parseInt(get(EConfigItem.TTN_GW_CACHE_EXPIRY_SEC.key)));
    }

    @Override
    public String getTtnPayloadEncoding() {
        return get(EConfigItem.TTN_PAYLOAD_ENCODING.key);
    }
    
}
