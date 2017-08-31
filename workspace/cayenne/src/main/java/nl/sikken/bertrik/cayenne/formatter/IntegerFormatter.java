package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Formatter for cayenne items which represent integer numbers.
 */
public final class IntegerFormatter extends BaseFormatter {

    private final int length;
    private final int size;
    private final boolean signed;

    /**
     * Constructor.
     * 
     * @param length the length of the return vector
     * @param size the size of each element
     * @param signed if the element is signed
     */
    public IntegerFormatter(int length, int size, boolean signed) {
        this.length = length;
        this.size = size;
        this.signed = signed;
    }

    @Override
    public String[] format(ByteBuffer bb) {
        final String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            final int value = getValue(bb, size, signed);
            values[i] = String.format(Locale.US, "%d", value);
        }
        return values;
    }
}
