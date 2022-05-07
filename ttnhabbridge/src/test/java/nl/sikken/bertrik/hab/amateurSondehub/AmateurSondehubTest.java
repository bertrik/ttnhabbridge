package nl.sikken.bertrik.hab.amateurSondehub;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import nl.sikken.bertrik.hab.Location;
import nl.sikken.bertrik.hab.Sentence;
import retrofit2.mock.Calls;

/**
 * Unit tests for AmateurSondehubUploader.
 */
public final class AmateurSondehubTest {
    
    private static final Location LOCATION = new Location(52.0162, 4.4735, 5.0);
    
    /**
     * Verifies creation of REST client.
     */
    @Test
    public void testCreateRestClient() {
        AmateurSondehubConfig config = new AmateurSondehubConfig();
    	Assert.assertNotNull(AmateurSondehubUploader.create(config));
    }
    
	/**
	 * Happy flow scenario for payload upload.
	 * @throws IOException 
	 */
	@Test
	public void testUploadPayload() throws IOException {
		// create a mocked rest client
		IAmateurSondehubRestApi restClient = Mockito.mock(IAmateurSondehubRestApi.class);
		Mockito.when(restClient.updateListener(Mockito.anyString(), Mockito.anyString()))
		        .thenReturn(Calls.response("OK"));
		
		AmateurSondehubUploader uploader = new AmateurSondehubUploader(restClient);
		
		// verify upload using the uploader
		uploader.start();
		try {
			HabReceiver receiver = new HabReceiver("BERTRIK", LOCATION);
			Instant instant = Instant.now();
			Sentence sentence = new Sentence("NOTAFLIGHT", 1, instant);
			sentence.addField("52.0182307,4.695772,1000");

			uploader.schedulePayloadTelemetryUpload(sentence.format(), Arrays.asList(receiver), instant);
            Mockito.verify(restClient, Mockito.timeout(3000).times(1)).updateListener(Mockito.anyString(),
                    Mockito.anyString());
		} finally {
			uploader.stop();
		}
	}

	

	
}