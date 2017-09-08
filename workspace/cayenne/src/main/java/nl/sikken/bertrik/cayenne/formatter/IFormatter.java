package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;

/**
 * Interface for cayenne data structures that can be formatted as an array of strings. 
 */
public interface IFormatter {

    /**
     * Parses raw data into an array of doubles
     * 
     * @param bb the byte buffer
     * @return the parsed value as a number
     */
    Double[] parse(ByteBuffer bb);

    /** 
     * Formats the data into an array of strings.
     * For example, for a GPS location it outputs: latitude in [0], longitude in [1], altitude in [2].
     * 
     * @param values the value as double array
     * @return the string representation
     */
    String[] format(Double[] values);

}
