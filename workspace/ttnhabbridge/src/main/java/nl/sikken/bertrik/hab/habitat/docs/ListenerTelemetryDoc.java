package nl.sikken.bertrik.hab.habitat.docs;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.sikken.bertrik.hab.habitat.IHabReceiver;

/**
 * Listener telemetry doc.
 */
public final class ListenerTelemetryDoc extends ListenerDoc {

    private final IHabReceiver receiver;

    /**
     * Constructor.
     * 
     * @param date the date
     * @param receiver the receiver info
     */
    public ListenerTelemetryDoc(Date date, IHabReceiver receiver) {
        super("listener_telemetry", date);
        this.receiver = receiver;
    }

    @Override
    protected JsonNode createDataNode() {
        final ObjectNode node = factory().objectNode();
        node.set("callsign", factory().textNode(receiver.getCallsign()));
        node.set("latitude", factory().numberNode(receiver.getLocation().getLat()));
        node.set("longitude", factory().numberNode(receiver.getLocation().getLon()));
        node.set("altitude", factory().numberNode(receiver.getLocation().getAlt()));
        return node;
    }

}
