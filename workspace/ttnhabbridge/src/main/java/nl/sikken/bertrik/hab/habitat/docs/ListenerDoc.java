package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Abstract listener doc, base class for specific listener docs.
 */
public abstract class ListenerDoc {

//    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final JsonNodeFactory factory = new JsonNodeFactory(false);

    private final OffsetDateTime date;
    private final String type;

    /**
     * Constructor.
     * @param instant the creation/upload date
     * @param type the document type
     */
    protected ListenerDoc(Instant instant, String type) {
        this.type = type;
        this.date = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * @return the JSON representation
     */
    public String format() {
        final ObjectNode topNode = factory.objectNode();
        topNode.set("type", factory.textNode(type));
        topNode.set("time_created", factory.textNode(dateFormat.format(date)));
        topNode.set("time_uploaded", factory.textNode(dateFormat.format(date)));
        topNode.set("data", createDataNode());

        return topNode.toString();
    }

    /**
     * @return the json node factory
     */
    protected final JsonNodeFactory factory() {
        return factory;
    }

    /**
     * Creates the sub-type specific 'data' part of the listener doc.
     * 
     * @return the 'data' node contents
     */
    abstract JsonNode createDataNode();

}
