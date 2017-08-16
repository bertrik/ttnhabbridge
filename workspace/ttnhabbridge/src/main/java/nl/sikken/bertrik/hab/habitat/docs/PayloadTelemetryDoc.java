package nl.sikken.bertrik.hab.habitat.docs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author bertrik
 *
 */
public final class PayloadTelemetryDoc {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final String callSign;
    private final byte[] rawBytes;
    private final Date dateCreated;
    private final Date dateUploaded;

    /**
     * Constructor.
     * 
     * @param date the date
     * @param callSign the receiver call sign
     * @param rawBytes the raw telemetry string as bytes
     */
    public PayloadTelemetryDoc(Date date, String callSign, byte[] rawBytes) {
        this.callSign = callSign;
        this.rawBytes = rawBytes;
        this.dateCreated = date;
        this.dateUploaded = date;
    }

    /**
     * @return the payload telemetry doc as JSON string
     */
    public String format() {
        final JsonNodeFactory factory = new JsonNodeFactory(false);
        final ObjectNode topNode = factory.objectNode();

        // create data node
        final ObjectNode dataNode = factory.objectNode();
        dataNode.set("_raw", factory.binaryNode(rawBytes));

        // create receivers node
        final ObjectNode receiverNode = factory.objectNode();
        receiverNode.set("time_created", factory.textNode(dateFormat.format(dateCreated)));
        receiverNode.set("time_uploaded", factory.textNode(dateFormat.format(dateUploaded)));
        final ObjectNode receiversNode = factory.objectNode();
        receiversNode.set(callSign, receiverNode);

        // put it together in the top node
        topNode.set("data", dataNode);
        topNode.set("receivers", receiversNode);

        return topNode.toString();
    }
}
