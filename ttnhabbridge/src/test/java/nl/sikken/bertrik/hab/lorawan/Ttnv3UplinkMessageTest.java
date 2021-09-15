package nl.sikken.bertrik.hab.lorawan;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.lorawan.LoraWanUplinkMessage.GatewayInfo;

public final class Ttnv3UplinkMessageTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Test
    public void testDecode() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("ttnv3_uplink.json");
        Ttnv3UplinkMessage message = MAPPER.readValue(is, Ttnv3UplinkMessage.class);

        LoraWanUplinkMessage uplinkMessage = message.toLoraWanUplinkMessage();
        
        Assert.assertEquals("test2id", uplinkMessage.getAppId());
        Assert.assertEquals("v3demo1", uplinkMessage.getDevId());
        Assert.assertEquals(1, uplinkMessage.getPort());
        Assert.assertEquals(84, uplinkMessage.getFcnt());
        
        List<GatewayInfo> gateways = uplinkMessage.getGateways();
        GatewayInfo gw = gateways.get(0);
        Assert.assertEquals("eui-024b08fefe040083", gw.getId());
        Assert.assertEquals(52.0, gw.getLocation().getLat(), 0.1);
        Assert.assertEquals(4.7, gw.getLocation().getLon(), 0.1);
        Assert.assertFalse(Double.isFinite(gw.getLocation().getAlt()));
    }
    
}
