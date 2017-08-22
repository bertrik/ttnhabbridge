package nl.sikken.bertrik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

        MQTT_SERVER_URL("mqtt.serverurl", "eu.thethings.network", "URL to MQTT server"),
        MQTT_CLIENT_ID("mqtt.clientid", "ttnhabbridge", "MQTT client id"),
        MQTT_USER_NAME("mqtt.username", "ttnmapper", "TTN application name used as MQTT user name"), 
        MQTT_USER_PASS("mqtt.password", "ttn-account-v2.Xc8BFRKeBK5nUhc9ikDcR-sbelgSMdHKnOQKMAiwpgI", "TTN application password"),
        MQTT_TOPIC("mqtt.topic", "ttnmapper/devices/+/up", "MQTT topic to subscribe to")
        ;
        
        private String key;
        private String def;
        private String comment;

        EConfigItem(String key, String def, String comment) {
            this.key = key;
            this.def = def;
            this.comment = comment;
        }
    }
    
    private final Map<EConfigItem, String> props = new HashMap<>();
    
    /**
     * Create a configuration setting with defaults.
     */
    public TtnHabBridgeConfig() {
        for (EConfigItem e : EConfigItem.values()) {
            props.put(e, e.def);
        }
    }
    
    /**
     * Load settings from file.
     * 
     * @param file the file
     * @throws IOException in case of a problem reading the file
     */
    public void load(File file) throws IOException {
        final Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        }
        for (EConfigItem e : EConfigItem.values()) {
            String value = properties.getProperty(e.key);
            if (value != null) {
                props.put(e, value);
            }
        }
    }
    
    /**
     * Save settings to file.
     * 
     * @param file the file
     * @throws IOException in case of a file problem
     */
    public void save(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
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
        return Integer.valueOf(props.get(EConfigItem.HABITAT_TIMEOUT));
    }

    @Override
    public String getHabitatUrl() {
        return props.get(EConfigItem.HABITAT_URL);
    }

    @Override
    public String getMqttClientId() {
        return props.get(EConfigItem.MQTT_CLIENT_ID);
    }

    @Override
    public String getMqttUserName() {
        return props.get(EConfigItem.MQTT_USER_NAME);
    }

    @Override
    public char[] getMqttPassword() {
        return props.get(EConfigItem.MQTT_USER_PASS).toCharArray();
    }

    @Override
    public String getMqttServerUrl() {
        return props.get(EConfigItem.MQTT_SERVER_URL);
    }
    
    @Override 
    public String getMqttTopic() {
        return props.get(EConfigItem.MQTT_TOPIC);
    }

}
