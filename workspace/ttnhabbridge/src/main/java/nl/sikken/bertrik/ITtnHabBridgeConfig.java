package nl.sikken.bertrik;

/**
 * Configuration interface for the application.
 */
public interface ITtnHabBridgeConfig {

    String getHabitatUrl();
    int    getHabitatTimeout();

    String getMqttServerUrl();
    String getMqttClientId();
    String getMqttUserName();
    char[] getMqttPassword();
    String getMqttTopic();

}
