package nl.sikken.bertrik.hab;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.sikken.bertrik.hab.ttn.TtnMessage;

/**
 * Decodes a payload and encodes it into a UKHAS sentence.
 */
public final class PayloadDecoder {
    
    private static final Logger LOG = LoggerFactory.getLogger(PayloadDecoder.class);
    
    /**
     * Decodes a TTN message into a UKHAS sentence.
     * 
     * @param message the message as received from TTN
     * @return the UKHAS sentence
     */
    public Sentence decode(TtnMessage message) {
        // common fields
        final String callSign = message.getDevId();
        final int id = message.getCounter();
        final Instant time = message.getMetaData().getTime();
        
        // decide between two supported specific formats
        final ObjectNode fields = message.getPayloadFields();
        if (fields != null) {
            LOG.info("Decoding 'ftelkamp' message...");

            // TTN payload
            final double latitude = fields.get("lat").doubleValue();
            final double longitude = fields.get("lon").doubleValue();
            final double altitude = fields.get("gpsalt").doubleValue();
            return new Sentence(callSign, id, Date.from(time), latitude, longitude, altitude);
        } else {
            LOG.info("Decoding 'sodaqone' message...");
            
            // SODAQ payload
            final SodaqOnePayload sodaq = SodaqOnePayload.parse(message.getPayloadRaw());
            
            // construct a sentence
            final double latitude = sodaq.getLatitude();
            final double longitude = sodaq.getLongitude();
            final double altitude = sodaq.getAltitude();
            return new Sentence(callSign, id, Date.from(time), latitude, longitude, altitude);
        }        
    }
    
}
