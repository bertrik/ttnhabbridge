package nl.sikken.bertrik.hab.habitat;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import nl.sikken.bertrik.hab.Sentence;

/**
 * Unit tests for HabitatUploader.
 */
public final class HabitatUploaderTest {
    
	/**
	 * Happy flow scenario.
	 * 
	 * Verifies that a request to upload results in an actual upload.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMockedHappyFlow() throws InterruptedException {
		// create a mocked rest client
		final IHabitatRestApi restClient = Mockito.mock(IHabitatRestApi.class);
		Mockito.when(restClient.updateListener(Mockito.anyString(), Mockito.anyString())).thenReturn("OK");
		final HabitatUploader uploader = new HabitatUploader(restClient);
		
		// test it
		uploader.start();
		try {
			final String receiver = "BERTRIK";
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			uploader.uploadPayloadTelemetry(sentence.format(), Arrays.asList(receiver), date);
			
			Mockito.verify(restClient, Mockito.timeout(3000)).updateListener(Mockito.anyString(), Mockito.anyString());
		} finally {
			uploader.stop();
		}
	}

	@Test
//    @Ignore("this is not a junit test")
	public void testActualPayloadUpload() throws InterruptedException {
		final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org", 3000);
		final HabitatUploader uploader = new HabitatUploader(restClient);
		uploader.start();
		try {
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			final String receiver = "BERTRIK";
			uploader.uploadPayloadTelemetry(sentence.format(), Arrays.asList(receiver), date);
			Thread.sleep(3000);
		} finally {
			uploader.stop();
		}
	}
	
	/**
	 * Verifies upload of listener information and telemetry.
	 * @throws InterruptedException 
	 */
	@Test
	public void testActualListenerUpload() throws InterruptedException {
	    final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org", 3000);
	    final HabitatUploader uploader = new HabitatUploader(restClient);
	    try {
            final Date date = new Date();
            final HabReceiver receiver = new HabReceiver("BERTRIK", new Location(52.0182307, 4.695772, 15));
	        uploader.uploadListenerData(receiver, date);
	        Thread.sleep(3000);
	    } finally {
	        uploader.stop();
	    }
	}
	
}
