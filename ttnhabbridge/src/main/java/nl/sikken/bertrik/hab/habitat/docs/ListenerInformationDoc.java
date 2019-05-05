package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.sikken.bertrik.hab.habitat.HabReceiver;

/**
 * Listener information doc.
 * 
 * SEE http://habitat.habhub.org/jse/#schemas/listener_information.json
 * 
 * NOTE the document above does NOT describe all fields that are present in practice!
 * There are also fields like "radio", "antenna", "location", "name"
 */
public final class ListenerInformationDoc extends ListenerDoc {

    private final HabReceiver receiver;

    /**
     * Constructor.
     * 
     * @param instant the creation/upload date
     * @param receiver the HAB receiver
     */
    public ListenerInformationDoc(Instant instant, HabReceiver receiver) {
        super(instant, "listener_information");
        this.receiver = receiver;
    }

    @Override
    protected JsonNode createDataNode() {
        ObjectNode node = factory().objectNode();
        node.set("callsign", factory().textNode(receiver.getCallsign()));
        node.set("radio", factory().textNode("TheThingsNetwork"));
        String antenna = String.format(Locale.US, "%.0f m", receiver.getLocation().getAlt());
        node.set("antenna", factory().textNode(antenna));
        return node;
    }

}
