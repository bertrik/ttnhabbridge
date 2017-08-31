package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Formatter for cayenne items which represent a GPS position.
 */
public final class GpsFormatter extends BaseFormatter {

    @Override
    public String[] format(ByteBuffer bb) {
        final double lat = 1E-4 * getValue(bb, 3, true);
        final double lon = 1E-4 * getValue(bb, 3, true);
        final double alt = 1E-2 * getValue(bb, 3, true);
        return new String[] {
                String.format(Locale.US, "%.4f", lat), 
                String.format(Locale.US, "%.4f", lon),
                String.format(Locale.US, "%.2f", alt)
                }; 
    }

}
