package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.sikken.bertrik.hab.habitat.HabReceiver;

/**
 * Listener telemetry doc.
 * 
 * SEE http://habitat.habhub.org/jse/#schemas/listener_telemetry.json
 */
public final class ListenerTelemetryDoc extends ListenerDoc {

    private final HabReceiver receiver;

    /**
     * Constructor.
     * 
     * @param instant the creation/upload date
     * @param receiver the receiver info
     */
    public ListenerTelemetryDoc(Instant instant, HabReceiver receiver) {
        super(instant, "listener_telemetry");
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
