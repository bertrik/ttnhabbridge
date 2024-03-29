package nl.sikken.bertrik.hab.habitat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.sikken.bertrik.hab.habitat.docs.ListenerInformationDoc;
import nl.sikken.bertrik.hab.habitat.docs.ListenerTelemetryDoc;
import nl.sikken.bertrik.hab.habitat.docs.PayloadTelemetryDoc;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Habitat uploader.
 * 
 * Exchanges data with the habitat system. Call to ScheduleXXX methods are
 * non-blocking. All actions run on a single background thread for simplicity.
 */
public final class HabitatUploader {

    private static final Logger LOG = LoggerFactory.getLogger(HabitatUploader.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Encoder base64Encoder = Base64.getEncoder();
    private final MessageDigest sha256;

    private final IHabitatRestApi restClient;

    /**
     * Creates a new habitat uploader.
     * 
     * @param config the configuration
     * @return the habitat uploader
     */
    public static HabitatUploader create(HabitatConfig config) {
        LOG.info("Creating new habitat REST client with timeout {} for {}", config.getTimeout(), config.getUrl());
        Duration timeout = Duration.ofSeconds(config.getTimeout());
        OkHttpClient client = new OkHttpClient().newBuilder().callTimeout(timeout).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getUrl()).addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).client(client).build();
        IHabitatRestApi restClient = retrofit.create(IHabitatRestApi.class);
        return new HabitatUploader(restClient);
    }

    /**
     * Constructor.
     * 
     * @param restClient the REST client used for uploading
     */
    HabitatUploader(IHabitatRestApi restClient) {
        try {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // this is fatal
            throw new IllegalStateException("No SHA-256 hash found", e);
        }
        this.restClient = restClient;
    }

    /**
     * Starts the uploader process.
     */
    public void start() {
        LOG.info("Starting habitat uploader");
    }

    /**
     * Stops the uploader process.
     */
    public void stop() {
        LOG.info("Stopping habitat uploader");
        executor.shutdown();
    }

    /**
     * Schedules a new sentence to be sent to the HAB network.
     * 
     * @param sentence  the ASCII sentence
     * @param receivers list of listener that received this sentence
     * @param instant   the current date/time
     */
    public void schedulePayloadTelemetryUpload(String sentence, List<HabReceiver> receivers, Instant instant) {
        LOG.info("Uploading for {} receivers: {}", receivers.size(), sentence.trim());

        // encode sentence as raw bytes
        byte[] bytes = sentence.getBytes(StandardCharsets.US_ASCII);

        // determine docId
        String docId = createDocId(bytes);

        for (HabReceiver receiver : receivers) {
            // create Json
            PayloadTelemetryDoc doc = new PayloadTelemetryDoc(instant, bytes);
            doc.addCallSign(receiver.getCallsign());
            String json = doc.format();

            // submit it to our processing thread
            executor.execute(() -> uploadPayloadTelemetry(docId, json));
        }
    }

    /**
     * Performs the actual payload telemetry upload as a REST-like call towards
     * habitat.
     * 
     * @param docId the document id
     * @param json  the JSON payload
     */
    private void uploadPayloadTelemetry(String docId, String json) {
        LOG.info("Upload payload telemetry doc {}: {}", docId, json);
        try {
            Response<String> response = restClient.updateListener(docId, json).execute();
            if (response.isSuccessful()) {
                LOG.info("Result payload telemetry doc {}: {}", docId, response.body());
            } else {
                LOG.warn("Result payload telemetry doc {}: {}", docId, response.message());
            }
        } catch (IOException e) {
            LOG.warn("Caught IOException: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Caught Exception: {}", e);
        }
    }

    /**
     * Creates the document id from the raw payload telemetry sentence.
     * 
     * @param bytes the raw sentence
     * @return the document id
     */
    private String createDocId(byte[] bytes) {
        byte[] base64 = base64Encoder.encode(bytes);
        byte[] hash = sha256.digest(base64);
        return DatatypeConverter.printHexBinary(hash).toLowerCase(Locale.ROOT);
    }

    /**
     * Schedules new listener data to be sent to habitat.
     * 
     * @param receiver the receiver data
     * @param instant  the current date/time
     */
    public void scheduleListenerDataUpload(HabReceiver receiver, Instant instant) {
        executor.execute(() -> uploadListener(receiver, instant));
    }

    /**
     * Uploads listener data (information and telemetry)
     * 
     * @param receiver the receiver/listener
     * @param instant  the current date/time
     */
    private void uploadListener(HabReceiver receiver, Instant instant) {
        LOG.info("Upload listener data for {}", receiver);
        try {
            // get two uuids
            LOG.info("Getting UUIDs for listener data upload...");
            UuidsList list = restClient.getUuids(2).execute().body();
            List<String> uuids = list.getUuids();
            if ((uuids != null) && (uuids.size() >= 2)) {
                LOG.info("Got {} UUIDs", uuids.size());

                // upload payload listener info
                LOG.info("Upload listener info using UUID {}...", uuids.get(0));
                ListenerInformationDoc info = new ListenerInformationDoc(instant, receiver);
                UploadResult infoResult = restClient.uploadDocument(uuids.get(0), info.format()).execute().body();
                LOG.info("Result listener info: {}", infoResult);

                // upload payload telemetry
                LOG.info("Upload listener telemetry using UUID {}...", uuids.get(1));
                ListenerTelemetryDoc telem = new ListenerTelemetryDoc(instant, receiver);
                UploadResult telemResult = restClient.uploadDocument(uuids.get(1), telem.format()).execute().body();
                LOG.info("Result listener telemetry: {}", telemResult);
            } else {
                LOG.warn("Did not receive UUIDs for upload");
            }
        } catch (IOException e) {
            LOG.warn("Caught IOException: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Caught Exception: {}", e);
        }
    }

}
