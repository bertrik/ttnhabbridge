package nl.sikken.bertrik.hab.lorawan;

/**
 * Interface of the callback from the TTN listener.
 */
public interface IMessageReceived {

    /**
     * Indicates that a message was received.
     * @param message the message
     */
    void messageReceived(LoraWanUplinkMessage message);
    
}
