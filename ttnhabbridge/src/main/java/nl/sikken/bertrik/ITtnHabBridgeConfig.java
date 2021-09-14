package nl.sikken.bertrik;

import java.time.Duration;

/**
 * Configuration interface for the application.
 */
public interface ITtnHabBridgeConfig {

    /**
     * @return URL of the habitat server
     */
    String getHabitatUrl();
    
    /**
     * @return timeout for communication with the habitat server
     */
    Duration getHabitatTimeout();
    
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
     * @return the expiry time of a gateway, i.e. the interval of listener information an telemetry uploads. 
     */
    Duration getTtnGwCacheExpiry();
    
    /**
     * @return the payload encoding, can be "sodaq", "json", "cayenne"
     */
    String getTtnPayloadEncoding();

}
