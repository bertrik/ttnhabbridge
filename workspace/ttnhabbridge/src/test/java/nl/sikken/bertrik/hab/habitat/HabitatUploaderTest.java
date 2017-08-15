package nl.sikken.bertrik.hab.habitat;

import java.util.Arrays;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import nl.sikken.bertrik.hab.Sentence;

/**
 * @author bertrik
 *
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
			final IHabReceiver receiver = createReceiver("BERTRIK", new Location(52.0182307, 4.695772, 4));
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			uploader.upload(sentence.format(), Arrays.asList(receiver), date);
			
			Mockito.verify(restClient, Mockito.timeout(3000)).updateListener(Mockito.anyString(), Mockito.anyString());
		} finally {
			uploader.stop();
		}
	}

	private IHabReceiver createReceiver(String callSign, Location location) {
		final IHabReceiver receiver = new IHabReceiver() {
			@Override
			public Location getLocation() {
				return location;
			}
			@Override
			public String getCallsign() {
				return callSign;
			}
		};
		return receiver;
	}

	@Test
    @Ignore("this is not a junit test")
	public void testActualHappyFlow() {
		final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org/habitat", 3000);
		final HabitatUploader uploader = new HabitatUploader(restClient);
		uploader.start();
		try {
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			final IHabReceiver receiver = createReceiver("BERTRIK", new Location(52.0182307, 4.695772, 4));
			uploader.upload(sentence.format(), Arrays.asList(receiver), date);
		} finally {
			uploader.stop();
		}
	}
	
}
