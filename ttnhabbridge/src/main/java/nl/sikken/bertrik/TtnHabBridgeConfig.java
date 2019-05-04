package nl.sikken.bertrik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration class.
 */
public final class TtnHabBridgeConfig implements ITtnHabBridgeConfig {
    
    /**
     * One enumeration item per configuration item.
     */
    private enum EConfigItem {
        HABITAT_URL("habitat.url", "http://habitat.habhub.org", "URL of the habitat server"),
        HABITAT_TIMEOUT("habitat.timeout", "3000", "Timeout in milliseconds"),

        TTN_MQTT_URL("ttn.mqtt.url", "tcp://eu.thethings.network", "URL of the TTN MQTT server"),
        TTN_APP_ID("ttn.app.id", "habhub", "TTN Application Id (e.g. habhub, ttnmapper, etc.)"),
        TTN_APP_KEY("ttn.app.key", "ttn-account-v2.Sh49WL90oQz-ZuxoDrS6yKuACL_jtAA0agdDfO_eVj4", "TTN Application key"),
        TTN_GW_CACHE_EXPIRY("ttn.gwcache.expiry", "600", "Gateway cache expiration time (seconds)"),
        TTN_PAYLOAD_ENCODING("ttn.payload.encoding", "sodaqone",
                "Payload format, allowed values: 'sodaqone','json','cayenne'"),
        ;
        
        private final String key;
        private final String def;
        private final String comment;

        EConfigItem(String key, String def, String comment) {
            this.key = key;
            this.def = def;
            this.comment = comment;
        }
    }
    
    private final Map<EConfigItem, String> props = new HashMap<>();
    
    /**
     * Constructor.
     * 
     * Configures all settings to their default value.
     */
    public TtnHabBridgeConfig() {
        for (EConfigItem e : EConfigItem.values()) {
            props.put(e, e.def);
        }
    }
    
    /**
     * Load settings from stream.
     * 
     * @param is input stream containing the settings
     * @throws IOException in case of a problem reading the file
     */
    public void load(InputStream is) throws IOException {
        final Properties properties = new Properties();
        properties.load(is);
        for (EConfigItem e : EConfigItem.values()) {
            String value = properties.getProperty(e.key);
            if (value != null) {
                props.put(e, value);
            }
        }
    }
    
    /**
     * Save settings to stream.
     * 
     * @param os the output stream
     * @throws IOException in case of a file problem
     */
    public void save(OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.US_ASCII)) {
            for (EConfigItem e : EConfigItem.values()) {
                // comment line
                writer.append("# " + e.comment + "\n");
                writer.append(e.key + "=" + e.def + "\n");
                writer.append("\n");
            }
        }
    }
    
    @Override
    public int getHabitatTimeout() {
        return Integer.parseInt(props.get(EConfigItem.HABITAT_TIMEOUT));
    }

    @Override
    public String getHabitatUrl() {
        return props.get(EConfigItem.HABITAT_URL);
    }

    @Override
    public String getTtnMqttUrl() {
        return props.get(EConfigItem.TTN_MQTT_URL);
    }

    @Override 
    public String getTtnAppId() {
        return props.get(EConfigItem.TTN_APP_ID);
    }
    
    @Override 
    public String getTtnAppKey() {
        return props.get(EConfigItem.TTN_APP_KEY);
    }

    @Override
    public int getTtnGwCacheExpiry() {
        return Integer.parseInt(props.get(EConfigItem.TTN_GW_CACHE_EXPIRY));
    }

    @Override
    public String getTtnPayloadEncoding() {
        return props.get(EConfigItem.TTN_PAYLOAD_ENCODING);
    }
    
}