package seedu.address.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for CSV file comparison in tests
 */
public class CsvTestUtil {
    /**
     * Compares two CSV files for equality of content
     */
    public static void assertFileContentMatch(Path actual, Path expected) throws IOException {
        List<String> actualLines = Files.readAllLines(actual).stream()
                .map(String::trim).collect(Collectors.toList());
        List<String> expectedLines = Files.readAllLines(expected).stream()
                .map(String::trim).collect(Collectors.toList());

        assertEquals(expectedLines.size(), actualLines.size(),
                "Line count mismatch between files");

        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals(expectedLines.get(i), actualLines.get(i),
                    "Line " + (i + 1) + " mismatch");
        }
    }
}
