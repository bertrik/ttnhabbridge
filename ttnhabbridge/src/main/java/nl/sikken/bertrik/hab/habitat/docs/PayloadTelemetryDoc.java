package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Payload telemetry document.
 * 
 * SEE http://habitat.habhub.org/jse/#schemas/payload_telemetry.json
 */
public final class PayloadTelemetryDoc {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final OffsetDateTime dateCreated;
    private final OffsetDateTime dateUploaded;
    private final List<String> callSigns = new ArrayList<>();
    private final byte[] rawBytes;

    /**
     * Constructor.
     * 
     * @param instant  the creation/upload date/time
     * @param rawBytes the raw telemetry string as bytes
     */
    public PayloadTelemetryDoc(Instant instant, byte[] rawBytes) {
        this.dateCreated = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        this.dateUploaded = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        this.rawBytes = rawBytes.clone();
    }

    public void addCallSign(String callSign) {
        this.callSigns.add(callSign);
    }

    /**
     * @return the payload telemetry doc as JSON string
     */
    public String format() {
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ObjectNode topNode = factory.objectNode();

        // create data node
        ObjectNode dataNode = factory.objectNode();
        dataNode.set("_raw", factory.binaryNode(rawBytes));

        // create receivers node
        ObjectNode receiversNode = factory.objectNode();
        for (String callSign : callSigns) {
            ObjectNode receiverNode = factory.objectNode();
            receiverNode.set("time_created", factory.textNode(dateFormat.format(dateCreated)));
            receiverNode.set("time_uploaded", factory.textNode(dateFormat.format(dateUploaded)));
            receiversNode.set(callSign, receiverNode);
        }

        // put it together in the top node
        topNode.set("data", dataNode);
        topNode.set("receivers", receiversNode);

        return topNode.toString();
    }
}
