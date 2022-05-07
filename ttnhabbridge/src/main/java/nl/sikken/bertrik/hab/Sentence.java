package nl.sikken.bertrik.hab;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Representation of a HAB telemetry sentence.
 */
public final class Sentence {

    private final String callSign;
    private final int id;
    private final Instant time;

    private final CrcCcitt16 crc16 = new CrcCcitt16();

    private final List<Entry<String, String>> fields = new ArrayList<>();

    /**
     * Constructor with the basic set of fields.
     * 
     * @param callSign the call sign
     * @param id message sequence number
     * @param time the creation time
     */
    public Sentence(String callSign, int id, Instant time) {
        this.callSign = callSign;
        this.id = id;
        this.time = Instant.from(time);
    }

    /**
     * Adds a pre-formatted extra field (optional).
     * 
     * @param value the pre-formatted value
     */
    public void addField(String fieldName, String fieldValueStr) {
        Entry<String, String> fieldPair = new SimpleEntry<>(fieldName, fieldValueStr);
        fields.add(fieldPair);
    }

    /**
     * Formats the sentence into an ASCII string.
     * 
     * @return a sentence formatted according to the basic UKHAS convention
     */
    @SuppressFBWarnings(value = "VA_FORMAT_STRING_USES_NEWLINE", justification = "use \n as specified")
    public String format() {
        // format time
        LocalDateTime local = LocalDateTime.ofInstant(time, ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT);
        String timeString = local.format(formatter);

        // format basic string
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ROOT, "%s,%d,%s", callSign, id, timeString));
        for (Entry<String, String> s : fields) {
            sb.append(',');
            sb.append(s.getValue());
        }
        String basic = sb.toString();

        // append header, checksum, etc
        byte[] bytes = basic.getBytes(StandardCharsets.US_ASCII);
        int crcValue = crc16.calculate(bytes, 0xFFFF);
        return String.format(Locale.ROOT, "$$%s*%04X\n", basic, crcValue);
    }

    @Override
    public String toString() {
        return format();
    }
    
}
