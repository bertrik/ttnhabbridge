package nl.sikken.bertrik;

/**
 * Configuration interface for the application.
 */
public interface ITtnHabBridgeConfig {

    /**
     * @return URL of the habitat server
     */
    String getHabitatUrl();
    
    /**
     * @return timeout (ms) for communication with the habitat server
     */
    int    getHabitatTimeout();
    
    /**
     * @return the URL of the TTN MQTT server
     */
    String getTtnMqttUrl();
    
    /**
     * @return the application id of the TTN application
     */
    String getTtnAppId();
    
    /**
     * @return the application key of the TTN application
     */
    String getTtnAppKey();
    
    /**
     * @return the expiry time (seconds) of a gateway, i.e. the interval of listener information an telemetry uploads. 
     */
    int getTtnGwCacheExpiry();
    
    /**
     * @return the payload encoding, can be "sodaq", "json", "cayenne"
     */
    String getTtnPayloadEncoding();

}
