package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;

/**
 * Interface for cayenne data structures that can be formatted as an array of strings. 
 */
public interface IFormatter {

    /** 
     * Formats the data into an array of strings.
     * For example, for a GPS location it outputs: latitude in [0], longitude in [1], altitude in [2].
     * 
     * @param bb the byte buffer containing the data
     * @return the string representation
     */
    String[] format(ByteBuffer bb);

}
