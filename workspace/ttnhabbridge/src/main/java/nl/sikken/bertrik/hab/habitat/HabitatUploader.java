package nl.sikken.bertrik.hab.habitat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import nl.sikken.bertrik.hab.habitat.docs.ListenerInformationDoc;
import nl.sikken.bertrik.hab.habitat.docs.ListenerTelemetryDoc;
import nl.sikken.bertrik.hab.habitat.docs.PayloadTelemetryDoc;

/**
 * Habitat uploader.
 * 
 * Exchanges data with the habitat system.
 * All actions run on a single thread for simplicity.
 */
public final class HabitatUploader {
    
    private static final Logger LOG = LoggerFactory.getLogger(HabitatUploader.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Encoder base64Encoder = Base64.getEncoder();
    private final MessageDigest sha256;

    private final IHabitatRestApi restClient;

    /**
     * Creates an actual REST client. Can be used in the constructor.
     * 
     * @param url the URL to connect to
     * @param timeout the connect and read timeout (ms)
     * @return a new REST client
     */
    public static IHabitatRestApi newRestClient(String url, int timeout) {
        // create the REST client
        LOG.info("Creating new habitat REST client with timeout {} for {}", timeout, url);
        final WebTarget target = ClientBuilder.newClient().property(ClientProperties.CONNECT_TIMEOUT, timeout)
                .property(ClientProperties.READ_TIMEOUT, timeout).target(url);
        return WebResourceFactory.newResource(IHabitatRestApi.class, target);
    }

    /**
     * Constructor.
     * 
     * @param restClient the REST client used for uploading
     */
    public HabitatUploader(IHabitatRestApi restClient) {
        try {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // this is fatal
            throw new IllegalStateException("No SHA-256 hash found");
        }
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
     * Schedules a new sentence to be sent to the HAB network.
     * 
     * @param sentence the ASCII sentence
     * @param receivers list of receivers that got this sentence
     * @param date the current date
     */
    public void schedulePayloadTelemetryUpload(String sentence, List<HabReceiver> receivers, Date date) {
        // encode sentence as raw bytes
        final byte[] bytes = sentence.getBytes(StandardCharsets.US_ASCII);

        // determine docId
        final String docId = createDocId(bytes);

        for (HabReceiver receiver : receivers) {
            LOG.info("Uploading for {}: {}", receiver, sentence.trim());

            // create Json
            final PayloadTelemetryDoc doc = new PayloadTelemetryDoc(date, receiver.getCallsign(), bytes);
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
        LOG.info("Upload payload telemetry doc {}: {}", docId, json);
        try {
            final String response = restClient.updateListener(docId, json);
            LOG.info("Result payload telemetry doc {}: {}", docId, response);
        } catch (WebApplicationException e) {
            LOG.warn("Caught exception: {}", e.getMessage());
        }
    }

    /**
     * Creates the document id from the raw payload telemetry sentence.
     * 
     * @param bytes the raw sentence
     * @return the document id
     */
    private String createDocId(byte[] bytes) {
        final byte[] base64 = base64Encoder.encode(bytes);
        final byte[] hash = sha256.digest(base64);
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }

    /**
     * Schedules new listener data to be sent to habitat.
     * 
     * @param receiver the receiver data
     * @param date the current date
     */
    public void scheduleListenerDataUpload(HabReceiver receiver, Date date) {
        executor.submit(() -> uploadListener(receiver, date));
    }
    
    /**
     * Uploads listener data (information and telemetry)
     * 
     * @param receiver the receiver/listener
     * @param date the current date
     */
    private void uploadListener(HabReceiver receiver, Date date) {
        LOG.info("Upload listener data for {}", receiver);
        try {
            // get two uuids
            LOG.info("Getting UUIDs for listener data upload...");
            final UuidsList list = restClient.getUuids(2);
            final List<String> uuids = list.getUuids();
            LOG.info("Got {} UUIDs", uuids.size());
            
            // upload payload listener info
            LOG.info("Upload listener info using UUID {}...", uuids.get(0));
            final ListenerInformationDoc info = new ListenerInformationDoc(date, receiver);
            final UploadResult infoResult = restClient.uploadDocument(uuids.get(0), info.format());
            LOG.info("Result listener info: {}", infoResult);
            
            // upload payload telemetry
            LOG.info("Upload listener telemetry using UUID {}...", uuids.get(1));
            final ListenerTelemetryDoc telem = new ListenerTelemetryDoc(date, receiver);
            final UploadResult telemResult = restClient.uploadDocument(uuids.get(1), telem.format());
            LOG.info("Result listener telemetry: {}", telemResult);
        } catch (Exception e) {
            LOG.warn("Caught WebServiceException: {}", e.getMessage());
        }
    }
    
}
