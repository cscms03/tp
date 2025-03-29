package seedu.address.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.commons.util.JsonUtil;

/**
 * Utility class for JSON file comparison in tests
 */
public class JsonTestUtil {
    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.getObjectMapper();

    /**
     * Compares two JSON files for equality of content
     */
    public static void assertFileContentMatch(Path actual, Path expected) throws IOException {
        JsonNode actualJson = OBJECT_MAPPER.readTree(Files.readString(actual));
        JsonNode expectedJson = OBJECT_MAPPER.readTree(Files.readString(expected));

        assertEquals(expectedJson, actualJson,
                "JSON content mismatch between " + expected + " and " + actual);
    }
}
