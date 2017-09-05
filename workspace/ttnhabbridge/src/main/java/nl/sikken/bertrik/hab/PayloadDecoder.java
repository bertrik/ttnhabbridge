package nl.sikken.bertrik.hab;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.sikken.bertrik.hab.ttn.TtnMessage;

/**
 * Decodes a payload and encodes it into a UKHAS sentence.
 */
public final class PayloadDecoder {
    
    private static final Logger LOG = LoggerFactory.getLogger(PayloadDecoder.class);
    
    private final EPayloadEncoding encoding;
    
    /**
     * Constructor.
     * 
     * @param encodingName the payload encoding name
     */
    public PayloadDecoder(String encodingName) {
        this.encoding = Objects.requireNonNull(EPayloadEncoding.parse(encodingName));
    }
    
    /**
     * Decodes a TTN message into a UKHAS sentence.
     * 
     * @param message the message as received from TTN
     * @return the UKHAS sentence
     */
    public Sentence decode(TtnMessage message) {
        // common fields
        final String callSign = message.getDevId();
        final int counter = message.getCounter();

        // specific fields
        final Sentence sentence;
        switch (encoding) {
        case SODAQ_ONE:
            sentence = decodeSodaqOne(message, callSign, counter);
            break;
        case JSON:
            sentence = decodeJson(message, callSign, counter);
            break;
        case CAYENNE:
            // fall through
        default:
            throw new IllegalStateException("Unhandled encoding " + encoding);
        }
        
        return sentence;
    }
    
    /**
     * Decodes a sodaqone encoded payload.
     * 
     * @return the UKHAS sentence
     */
    private Sentence decodeSodaqOne(TtnMessage message, String callSign, int counter) {
        LOG.info("Decoding 'sodaqone' message...");
        
        // SODAQ payload
        final SodaqOnePayload sodaq = SodaqOnePayload.parse(message.getPayloadRaw());
        
        // construct a sentence
        final double latitude = sodaq.getLatitude();
        final double longitude = sodaq.getLongitude();
        final double altitude = sodaq.getAltitude();
        final Date time = new Date(1000L * sodaq.getTimeStamp());
        final Sentence sentence = new Sentence(callSign, counter, time, latitude, longitude, altitude);
        sentence.addField(String.format(Locale.US, "%.0f", sodaq.getBoardTemp()));
        sentence.addField(String.format(Locale.US, "%.2f", sodaq.getBattVoltage()));
        return sentence;
    }
        
    /**
     * Decodes a JSON encoded payload.
     * 
     * @return the UKHAS sentence
     */
    private Sentence decodeJson(TtnMessage message, String callSign, int counter) {
        LOG.info("Decoding 'json' message...");

        // TTN payload
        final Instant time = message.getMetaData().getTime();
        final ObjectNode fields = message.getPayloadFields();
        final double latitude = fields.get("lat").doubleValue();
        final double longitude = fields.get("lon").doubleValue();
        final double altitude = fields.get("gpsalt").doubleValue();
        final Sentence sentence = new Sentence(callSign, counter, Date.from(time), latitude, longitude, altitude);
        final JsonNode tempNode = fields.get("temp");
        final JsonNode vccNode = fields.get("vcc");
        if ((tempNode != null) && (vccNode != null)) {
            sentence.addField(String.format(Locale.US, "%.1f", tempNode.doubleValue()));
            sentence.addField(String.format(Locale.US, "%.3f", vccNode.doubleValue()));
        }
        return sentence;
    }
    
}
