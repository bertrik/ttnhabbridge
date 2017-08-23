package nl.sikken.bertrik.hab.ttn;

/**
 * Interface of the callback from the TTN listener.
 */
public interface IMessageReceived {

    /**
     * Indicates that a message was received.
     * 
     * @param topic the topic
     * @param message the message
     */
    void messageReceived(String topic, String message);
    
}
