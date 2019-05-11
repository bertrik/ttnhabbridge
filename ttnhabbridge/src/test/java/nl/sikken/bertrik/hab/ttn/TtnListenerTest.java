package nl.sikken.bertrik.hab.ttn;

import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;
import org.mockito.Mockito;

public final class TtnListenerTest {

	@Test
	public void testListener() throws Exception {
		IMessageReceived listener = Mockito.mock(IMessageReceived.class);
		TtnListener ttnListener = new TtnListener(listener, "tcp://localhost", "appId", "appKey");
		String msg = "message";
		ttnListener.messageArrived("topic", new MqttMessage(msg.getBytes(StandardCharsets.US_ASCII)));
		
		// verify that the message is forwarded to the listener
		Mockito.verify(listener).messageReceived(Mockito.eq("topic"), Mockito.eq("message"));
	}
	
}
