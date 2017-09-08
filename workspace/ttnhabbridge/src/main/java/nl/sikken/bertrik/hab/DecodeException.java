package nl.sikken.bertrik.hab;

/**
 * Exception thrown when decoding a payload fails.
 */
public final class DecodeException extends Exception {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor.
     * 
     * @param message the exception message
     */
    public DecodeException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message the exception message
     * @param t the parent cause of the exception
     */
    public DecodeException(String message, Throwable t) {
        super(message, t);
    }

}
