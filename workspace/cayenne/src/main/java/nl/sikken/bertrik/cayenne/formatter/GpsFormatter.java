package nl.sikken.bertrik.cayenne.formatter;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Formatter for cayenne items which represent a GPS position.
 */
public final class GpsFormatter extends BaseFormatter {

    @Override
    public Double[] parse(ByteBuffer bb) {
        final double lat = 1E-4 * getValue(bb, 3, true);
        final double lon = 1E-4 * getValue(bb, 3, true);
        final double alt = 1E-2 * getValue(bb, 3, true);
        return new Double[] {lat, lon, alt};
    }

    @Override
    public String[] format(Double[] values) {
        return new String[] {
                String.format(Locale.US, "%.4f", values[0]), 
                String.format(Locale.US, "%.4f", values[1]),
                String.format(Locale.US, "%.2f", values[2])
                }; 
    }

}
