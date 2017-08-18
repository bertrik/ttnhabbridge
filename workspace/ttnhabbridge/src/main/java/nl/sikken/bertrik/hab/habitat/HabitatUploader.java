package nl.sikken.bertrik.hab.habitat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.xml.bind.DatatypeConverter;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.sikken.bertrik.hab.habitat.docs.PayloadTelemetryDoc;

/**
 * Habitat uploader.
 */
public final class HabitatUploader {

    private static MessageDigest sha256;

    private final Logger LOG = LoggerFactory.getLogger(HabitatUploader.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Encoder base64Encoder = Base64.getEncoder();
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final IHabitatRestApi restClient;

    static {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No SHA-256 hash found");
        }
    }

    /**
     * Constructor.
     * 
     * @param restClient the REST client used for uploading
     */
    public HabitatUploader(IHabitatRestApi restClient) {
        this.restClient = restClient;
    }

    /**
     * Starts the uploader process.
     */
    public void start() {
        LOG.info("Starting habitat uploader");

        LOG.info("Started habitat uploader");
    }

    /**
     * Stops the uploader process.
     */
    public void stop() {
        LOG.info("Stopping habitat uploader");
        executor.shutdown();
        LOG.info("Stopped habitat uploader");
    }

    /**
     * Uploads a new sentence to the HAB network (non-blocking).
     * 
     * @param sentence the ASCII sentence
     * @param receivers list of receivers that got this sentence
     * @param date the current date
     */
    public void upload(String sentence, List<String> receivers, Date date) {
        // encode sentence as raw bytes
        final byte[] bytes = sentence.getBytes(StandardCharsets.US_ASCII);

        // determine docId
        final String docId = createDocId(bytes);
        LOG.info("docid = {}", docId);

        for (String receiver : receivers) {
            LOG.info("Uploading for {}: {}", receiver, sentence.trim());

            // create Json
            final PayloadTelemetryDoc doc = new PayloadTelemetryDoc(date, receiver, bytes);
            final String json = doc.format();

            // submit it to our processing thread
            executor.submit(() -> uploadTelemetry(docId, json));
        }
    }

    /**
     * Performs the actual upload as a REST-like call towards habitat.
     * 
     * @param docId the document id
     * @param json the JSON payload
     */
    private void uploadTelemetry(String docId, String json) {
        LOG.info("Sending for {}: {}", docId, json);
        try {
            final String response = restClient.updateListener(docId, json);
            LOG.info("Response for {}: {}", docId, response);
        } catch (WebApplicationException e) {
            LOG.warn("Caught exception: {}", e.getMessage());
        }
    }

    /**
     * Creates the JSON payload.
     * 
     * @param receiver the radio receiver properties
     * @param bytes the raw sentence
     * @param dateCreated the creation date
     * @param dateUploaded the upload date
     * @return a new JSON encoded string
     */
    public String createJson(HabReceiver receiver, byte[] bytes, Date dateCreated, Date dateUploaded) {
        final JsonNodeFactory factory = new JsonNodeFactory(false);
        final ObjectNode topNode = factory.objectNode();

        // create data node
        final ObjectNode dataNode = factory.objectNode();
        dataNode.set("_raw", factory.binaryNode(bytes));

        // create receivers node
        final ObjectNode receiversNode = factory.objectNode();
        final ObjectNode receiverNode = factory.objectNode();
        receiverNode.set("time_created", factory.textNode(dateFormat.format(dateCreated)));
        receiverNode.set("time_uploaded", factory.textNode(dateFormat.format(dateUploaded)));
        receiversNode.set(receiver.getCallsign(), receiverNode);

        // put it together in the top node
        topNode.set("data", dataNode);
        topNode.set("receivers", receiversNode);

        return topNode.toString();
    }

    /**
     * Creates the document id from the raw payload telemetry sentence.
     * 
     * @param bytes the raw sentence
     * @return the document id
     */
    public String createDocId(byte[] bytes) {
        final byte[] base64 = base64Encoder.encode(bytes);
        final byte[] hash = sha256.digest(base64);
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }

    public String createListenerInformation(String callSign, Date dateCreated, Date dateUploaded) {
        final JsonNodeFactory factory = new JsonNodeFactory(false);
        final ObjectNode topNode = factory.objectNode();
        topNode.set("type", factory.textNode("listener_information"));
        topNode.set("time_created", factory.textNode(dateFormat.format(dateCreated)));
        topNode.set("time_uploaded", factory.textNode(dateFormat.format(dateUploaded)));
        final ObjectNode callSignNode = factory.objectNode();
        callSignNode.set("callsign", factory.textNode(callSign));
        topNode.set("data", callSignNode);

        return topNode.toString();
    }

    /**
     * Creates an actual REST client. Can be used in the constructor.
     * 
     * @param url the URL to connect to
     * @param timeout the connect and read timeout (ms)
     * @return a new REST client
     */
    public static IHabitatRestApi newRestClient(String url, int timeout) {
        // create the REST client
        final WebTarget target = ClientBuilder.newClient().property(ClientProperties.CONNECT_TIMEOUT, timeout)
                .property(ClientProperties.READ_TIMEOUT, timeout).target(url);
        return WebResourceFactory.newResource(IHabitatRestApi.class, target);
    }

}
