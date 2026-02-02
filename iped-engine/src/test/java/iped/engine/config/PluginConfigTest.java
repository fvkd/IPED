package iped.engine.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import iped.utils.UTF8Properties;

public class PluginConfigTest {

    @Test
    public void testTskJarPathFromSystemProperty() {
        // Only run on non-Windows for this test as logic is specific
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return;
        }

        // Create a dummy jar file to point to
        File dummyJar = new File("dummy-sleuthkit.jar");
        try {
            dummyJar.createNewFile();

            // Set system property
            System.setProperty("iped.tsk.jar.path", dummyJar.getAbsolutePath());

            PluginConfig config = new PluginConfig();
            UTF8Properties props = new UTF8Properties();

            // Process properties (empty, so it should fallback to system prop)
            config.processProperties(props);

            File tskFile = config.getTskJarFile();
            assertNotNull(tskFile);
            assertEquals(dummyJar.getAbsolutePath(), tskFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            dummyJar.delete();
            System.clearProperty("iped.tsk.jar.path");
        }
    }
}
