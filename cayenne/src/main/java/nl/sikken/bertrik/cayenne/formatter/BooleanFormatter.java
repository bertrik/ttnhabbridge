package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Formatter for cayenne items which represent booleans.
 */
public final class BooleanFormatter extends BaseFormatter {

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
    public BooleanFormatter(int length, int size, boolean signed) {
        this.length = length;
        this.size = size;
        this.signed = signed;
    }

    @Override
    public Integer[] parse(ByteBuffer bb) {
        Integer[] values = new Integer[length];
        for (int i = 0; i < length; i++) {
            values[i] = (getValue(bb, size, signed) > 0) ? 1 : 0;
        }
        return values;
    }

    @Override
    public String[] format(Number[] values) {
        String[] formatted = new String[length];
        for (int i = 0; i < length; i++) {
            formatted[i] = String.format(Locale.ROOT, "%d", values[i].intValue());
        }
        return formatted;
    }

    @Override
    public void encode(ByteBuffer bb, Number[] values) {
        for (int i = 0; i < length; i++) {
            putValue(bb, 1, values[i].intValue() > 0 ? 1 : 0);
        }
    }

}
