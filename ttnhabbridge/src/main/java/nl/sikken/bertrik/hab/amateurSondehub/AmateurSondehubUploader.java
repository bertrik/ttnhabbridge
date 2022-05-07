package nl.sikken.bertrik.hab.amateurSondehub;

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

import nl.sikken.bertrik.hab.HabReceiver;
import nl.sikken.bertrik.hab.amateurSondehub.AmateurSondehubConfig;
import nl.sikken.bertrik.hab.amateurSondehub.IAmateurSondehubRestApi;
import nl.sikken.bertrik.hab.UploadResult;
import nl.sikken.bertrik.hab.amateurSondehub.docs.ListenerInformationDoc;
import nl.sikken.bertrik.hab.amateurSondehub.docs.ListenerTelemetryDoc;
import nl.sikken.bertrik.hab.amateurSondehub.docs.PayloadTelemetryDoc;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * AmateurSondehub uploader.
 * 
 * Exchanges data with the amateurSondehub system. Call to ScheduleXXX methods
 * are non-blocking. All actions run on a single background thread for
 * simplicity.
 */
public final class AmateurSondehubUploader {

    private static final Logger LOG = LoggerFactory.getLogger(AmateurSondehubUploader.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final IAmateurSondehubRestApi restClient;

    /**
     * Creates a new amateurSondehub uploader.
     * 
     * @param config the configuration
     * @return the amateurSondehub uploader
     */
    public static AmateurSondehubUploader create(AmateurSondehubConfig config) {
        LOG.info("Creating new amateurSondehub REST client with timeout {} for {}", config.getTimeout(),
                config.getUrl());
        Duration timeout = Duration.ofSeconds(config.getTimeout());
        OkHttpClient client = new OkHttpClient().newBuilder().callTimeout(timeout).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).client(client).build();
        IAmateurSondehubRestApi restClient = retrofit.create(IAmateurSondehubRestApi.class);
        return new AmateurSondehubUploader(restClient);
    }

    /**
     * Constructor.
     * 
     * @param restClient the REST client used for uploading
     */
    AmateurSondehubUploader(IAmateurSondehubRestApi restClient) {
        this.restClient = restClient;
    }

    /**
     * Starts the uploader process.
     */
    public void start() {
        LOG.info("Starting amateurSondehub uploader");
    }

    /**
     * Stops the uploader process.
     */
    public void stop() {
        LOG.info("Stopping amateurSondehub uploader");
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


        for (HabReceiver receiver : receivers) {
            // create Json
            PayloadTelemetryDoc doc = new payloadTelemetryDoc(instant, bytes);
            doc.addCallSign(receiver.getCallsign());
            String json = doc.format();

            // submit it to our processing thread
            executor.execute(() -> uploadPayloadTelemetry(docId, json));
        }
    }

    /**
     * Performs the actual payload telemetry upload as a REST-like call towards
     * amateurSondehub.
     * 
     * @param json  the JSON payload
     */
    private void uploadPayloadTelemetry(String json) {
        LOG.info("Upload payload telemetry: {}", json);
        try {
            Response<String> response = restClient.updateListener(json).execute();
            if (response.isSuccessful()) {
                LOG.info("Result payload telemetry: {}", response.body());
            } else {
                LOG.warn("Result payload telemetry: {}", response.message());
            }
        } catch (IOException e) {
            LOG.warn("Caught IOException: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Caught Exception: {}", e);
        }
    }

}
