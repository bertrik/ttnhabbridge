package nl.sikken.bertrik.hab.habitat;

import java.util.Arrays;
import java.util.Date;

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
			final IHabReceiver receiver = createReceiver();
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			uploader.upload(sentence.format(), Arrays.asList(receiver), date);
			
			Mockito.verify(restClient, Mockito.timeout(3000)).updateListener(Mockito.anyString(), Mockito.anyString());
		} finally {
			uploader.stop();
		}
	}

	private IHabReceiver createReceiver() {
		final IHabReceiver receiver = new IHabReceiver() {
			@Override
			public Location getLocation() {
				return new Location(52.0182307, 4.695772, 4);
			}
			@Override
			public String getCallsign() {
				return "BERTRIK";
			}
		};
		return receiver;
	}

}
