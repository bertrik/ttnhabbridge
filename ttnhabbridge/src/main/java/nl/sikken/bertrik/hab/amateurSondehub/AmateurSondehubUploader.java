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

import nl.sikken.bertrik.hab.amateurSondehub.AmateurSondehubConfig;
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




}
