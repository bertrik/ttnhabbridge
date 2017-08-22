package nl.sikken.bertrik.hab.habitat.docs;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Listener information doc.
 * 
 * SEE http://habitat.habhub.org/jse/#schemas/listener_information.json
 * 
 * NOTE the document above does NOT describe all fields that are present in practice!
 * There are also fields like "radio", "antenna", "location", "name"
 */
public final class ListenerInformationDoc extends ListenerDoc {

    private final String callSign;

    /**
     * Constructor.
     * 
     * @param date the date
     * @param callSign the listener call sign
     */
    public ListenerInformationDoc(Date date, String callSign) {
        super("listener_information", date);
        this.callSign = callSign;
    }

    @Override
    protected JsonNode createDataNode() {
        final ObjectNode node = factory().objectNode();
        node.set("callsign", factory().textNode(callSign));
        node.set("radio", factory().textNode("TheThingsNetwork"));
        return node;
    }

}
