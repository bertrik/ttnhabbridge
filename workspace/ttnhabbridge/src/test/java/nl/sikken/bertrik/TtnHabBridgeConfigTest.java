package nl.sikken.bertrik;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for TtnHabBridgeConfig.
 */
public final class TtnHabBridgeConfigTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();    
    
    /**
     * Verifies basic loading/saving of a configuration.
     * 
     * @throws IOException
     */
    @Test
    public void testLoadSave() throws IOException {
        final TtnHabBridgeConfig config = new TtnHabBridgeConfig();
        final File file = new File(tempFolder.getRoot(), "test.properties");
        config.save(file);
        config.load(file);
    }

}
