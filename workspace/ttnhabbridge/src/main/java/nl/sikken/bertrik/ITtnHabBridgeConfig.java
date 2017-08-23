package nl.sikken.bertrik;

/**
 * Configuration interface for the application.
 */
public interface ITtnHabBridgeConfig {

    String getHabitatUrl();
    int    getHabitatTimeout();

    String getTtnMqttUrl();
    String getTtnAppId();
    String getTtnAppKey();

}
