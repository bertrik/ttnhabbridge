package nl.sikken.bertrik.hab;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Representation of a HAB telemetry sentence.
 */
public final class Sentence {

    private final String callSign;
    private final int id;
    private final Instant time;
    private final double latitude;
    private final double longitude;
    private final double altitude;

    private final CrcCcitt16 crc16 = new CrcCcitt16();

    private final List<String> extras = new ArrayList<>();

    /**
     * Constructor with all mandatory fields.
     * 
     * @param callSign the call sign
     * @param id some incrementing number
     * @param time the creation time
     * @param latitude the latitude (degrees)
     * @param longitude the longitude (degrees)
     * @param altitude the altitude (meter)
     */
    public Sentence(String callSign, int id, Instant time, double latitude, double longitude, double altitude) {
        this.callSign = callSign;
        this.id = id;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Adds a pre-formatted extra field (optional).
     * 
     * @param value the pre-formatted value
     */
    public void addField(String value) {
        extras.add(value);
    }

    /**
     * Formats the sentence into an ASCII string.
     * 
     * @return a sentence formatted according to the basic UKHAS convention
     */
    public String format() {
        // format time
        final LocalDateTime local = LocalDateTime.ofInstant(time, ZoneId.of("UTC"));
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
        final String timeString = local.format(formatter);

        // format basic string
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "%s,%d,%s,%.6f,%.6f,%.1f", callSign, id, timeString, latitude, longitude,
                altitude));
        for (String s : extras) {
            sb.append(',');
            sb.append(s);
        }
        final String basic = sb.toString();

        // append header, checksum, etc
        final byte[] bytes = basic.getBytes(StandardCharsets.US_ASCII);
        final int crcValue = crc16.calculate(bytes, 0xFFFF);
        return String.format(Locale.US, "$$%s*%04X\n", basic, crcValue);
    }

    @Override
    public String toString() {
        return format();
    }
    
}
