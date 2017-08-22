package nl.sikken.bertrik;

import java.io.File;
import java.io.IOException;

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
        final TtnHabBridgeConfig config = new TtnHabBridgeConfig();
        final File file = new File("test.properties");
        config.save(file);
        config.load(file);
    }

}
