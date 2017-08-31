package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;

/**
 * Abstract base class of formatters.
 */
public abstract class BaseFormatter implements IFormatter {

    /**
     * Gets a value from the byte buffer.
     * 
     * @param bb the byte buffer
     * @param n the number of bytes to get
     * @param signed whether it should be interpreted as signed value or not
     * @return the value
     */
    protected int getValue(ByteBuffer bb, int n, boolean signed) {
        int val = bb.get();
        val = signed ? val : val & 0xFF;
        for (int i = 1; i < n; i++) {
            val <<= 8;
            val += bb.get() & 0xFF;
        }
        return val;
    }
    
}
