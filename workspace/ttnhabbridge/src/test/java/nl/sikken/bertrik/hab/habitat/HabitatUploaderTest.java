package nl.sikken.bertrik.hab.habitat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.sikken.bertrik.hab.Sentence;
import nl.sikken.bertrik.hab.habitat.docs.ListenerInformationDoc;
import nl.sikken.bertrik.hab.habitat.docs.ListenerTelemetryDoc;

/**
 * Unit tests for HabitatUploader.
 */
public final class HabitatUploaderTest {
    
    private final Logger LOG = LoggerFactory.getLogger(HabitatUploaderTest.class);
	
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
			uploader.upload(sentence.format(), Arrays.asList(receiver), date);
			
			Mockito.verify(restClient, Mockito.timeout(3000)).updateListener(Mockito.anyString(), Mockito.anyString());
		} finally {
			uploader.stop();
		}
	}

	@Test
//    @Ignore("this is not a junit test")
	public void testActualHappyFlow() throws InterruptedException {
		final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org", 3000);
		final HabitatUploader uploader = new HabitatUploader(restClient);
		uploader.start();
		try {
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			final String receiver = "BERTRIK";
			uploader.upload(sentence.format(), Arrays.asList(receiver), date);
			Thread.sleep(3000);
		} finally {
			uploader.stop();
		}
	}
	
	/**
	 * Verifies upload of listener information and telemetry.
	 */
	@Test
	public void testListenerUpload() {
	    // get two uuids
	    final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org", 3000);
	    final UuidsList list = restClient.getUuids(3);
	    final List<String> uuids = list.getUuids();
	    LOG.info("list = {},{}", uuids.get(0), uuids.get(1));
	    
	    final Date date = new Date();
	    final String callSign = "BERTRIK";
	    
	    // upload payload listener info
	    final ListenerInformationDoc info = new ListenerInformationDoc(date, callSign);
	    restClient.uploadDocument(uuids.get(0), info.format());
	    
	    // upload payload telemetry
	    final Location location = new Location(52.0182307, 4.695772, 15);
	    final HabReceiver receiver = new HabReceiver(callSign, location);
	    final ListenerTelemetryDoc telem = new ListenerTelemetryDoc(date, receiver);
	    restClient.uploadDocument(uuids.get(1), telem.format());
	}
	
}
