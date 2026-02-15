package iped.parsers.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetadataUtilBenchmark {

    @Test
    public void testCorrectness() {
        assertEquals("HelloWorld", MetadataUtil.normalizeTerm("hello world"));
        assertEquals("HELLOWORLD", MetadataUtil.normalizeTerm("HELLO WORLD"));
        assertEquals("HelloWorld", MetadataUtil.normalizeTerm("   hello   world   "));
        assertEquals("123abcDEF", MetadataUtil.normalizeTerm("123abcDEF"));
        assertEquals("CaseTest", MetadataUtil.normalizeTerm("case-test"));
        assertEquals("", MetadataUtil.normalizeTerm("!!!"));
    }

    @Test
    public void benchmark() {
        String testString = "This is a test string for normalization. It should box and unbox many characters!";
        int iterations = 1_000_000;

        // Warmup
        for (int i = 0; i < 100_000; i++) {
            MetadataUtil.normalizeTerm(testString);
        }

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            MetadataUtil.normalizeTerm(testString);
        }
        long end = System.nanoTime();

        System.out.println("Time taken for " + iterations + " iterations: " + (end - start) / 1_000_000.0 + " ms");
    }
}
