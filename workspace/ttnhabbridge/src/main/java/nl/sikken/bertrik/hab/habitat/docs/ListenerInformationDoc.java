package nl.sikken.bertrik.hab.habitat.docs;

import java.util.Date;
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
     * @param date the date
     * @param callSign the listener call sign
     */
    public ListenerInformationDoc(Date date, HabReceiver receiver) {
        super("listener_information", date);
        this.receiver = receiver;
    }

    @Override
    protected JsonNode createDataNode() {
        final ObjectNode node = factory().objectNode();
        node.set("callsign", factory().textNode(receiver.getCallsign()));
        node.set("radio", factory().textNode("TheThingsNetwork"));
        final String antenna = String.format(Locale.US, "%.0f m", receiver.getLocation().getAlt());
        node.set("antenna", factory().textNode(antenna));
        return node;
    }

}
