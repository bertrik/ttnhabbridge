package nl.sikken.bertrik.hab.lorawan;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.lorawan.HeliumUplinkMessage.HotSpot;
import nl.sikken.bertrik.hab.lorawan.LoraWanUplinkMessage.GatewayInfo;

public final class HeliumUplinkMessageTest {

    @Test
    public void testDecode() throws IOException {
        HeliumUplinkMessage helium;

        // decode JSON
        try (InputStream is = this.getClass().getResourceAsStream("/helium_uplink.json")) {
            ObjectMapper mapper = new ObjectMapper();
            helium = mapper.readValue(is, HeliumUplinkMessage.class);
        }
        Assert.assertNotNull(helium);

        Assert.assertEquals(0, helium.fcnt);
        Assert.assertEquals("kissmapper", helium.name);
        Assert.assertEquals(1, helium.port);
        Assert.assertEquals(1631457565832L, helium.reportedAt);
        Assert.assertArrayEquals(new byte[] { 3 }, helium.payload);

        HotSpot hotSpot = helium.hotSpots.get(0);
        Assert.assertEquals(52.01745, hotSpot.latitude, 0.00001);
        Assert.assertEquals(4.729876, hotSpot.longitude, 0.00001);
        Assert.assertEquals("melted-quartz-antelope", hotSpot.name);
        Assert.assertEquals(-120, hotSpot.rssi, 0.1);
        Assert.assertEquals(-7.5, hotSpot.snr, 0.1);
        
        // decode to LoRaWAN message
        LoraWanUplinkMessage lorawan = helium.toLoraWanUplinkMessage();
        Assert.assertEquals(Instant.parse("2021-09-12T14:39:25.832Z"), lorawan.getTime());
        Assert.assertEquals("kissmapper", lorawan.getDevId());
        Assert.assertEquals(0, lorawan.getFcnt());
        Assert.assertEquals(1, lorawan.getPort());
        Assert.assertArrayEquals(new byte[] {3}, lorawan.getPayloadRaw());
        GatewayInfo gw = lorawan.getGateways().get(0);
        Assert.assertEquals(52.01745, gw.getLocation().getLat(), 0.00001);
        Assert.assertEquals(4.729876, gw.getLocation().getLon(), 0.00001);
        Assert.assertFalse(Double.isFinite(gw.getLocation().getAlt()));
    }

}
