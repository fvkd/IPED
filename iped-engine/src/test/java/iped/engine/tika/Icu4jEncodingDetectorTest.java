package iped.engine.tika;

import org.apache.tika.metadata.Metadata;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Icu4jEncodingDetectorTest {

    @Test
    public void testDetectWithInvalidEncoding() throws IOException {
        Icu4jEncodingDetector detector = new Icu4jEncodingDetector();
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_ENCODING, "invalid");

        // This should not throw an exception and should log a warning
        detector.detect(new ByteArrayInputStream(new byte[0]), metadata);
    }
}
