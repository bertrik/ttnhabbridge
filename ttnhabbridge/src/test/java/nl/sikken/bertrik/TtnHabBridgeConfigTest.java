package nl.sikken.bertrik;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for TtnHabBridgeConfig.
 */
public final class TtnHabBridgeConfigTest {
    
    /**
     * Verifies basic loading/saving of a configuration.
     * 
     * @throws IOException
     */
    @Test
    public void testLoadSave() throws IOException {
        byte[] data;
        // save
        TtnHabBridgeConfig config = new TtnHabBridgeConfig();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            config.save(os);
            data = os.toByteArray();
        }
        // load
        try (InputStream is = new ByteArrayInputStream(data)) {
            config.load(is);
        }
    }
    
    /**
     * Verifies that every getter returns a valid value.
     */
    @Test
    public void testAllProps() {
        TtnHabBridgeConfig config = new TtnHabBridgeConfig();
        Assert.assertNotNull(config.getHabitatUrl());
        Assert.assertNotNull(config.getHabitatTimeout());
        Assert.assertNotNull(config.getTtnMqttUrl());
        Assert.assertNotNull(config.getTtnAppId());
        Assert.assertNotNull(config.getTtnAppKey());
        Assert.assertNotNull(config.getTtnGwCacheExpiry());
    }

}
