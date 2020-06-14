package nl.sikken.bertrik.hab.ttn;

import java.time.Instant;

/**
 * Interface of the callback from the TTN listener.
 */
public interface IMessageReceived {

    /**
     * Indicates that a message was received.
     * 
     * @param now the arrival time
     * @param topic the topic
     * @param message the message
     */
    void messageReceived(Instant now, String topic, String message);
    
}
