package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

public class JsonCsvConverterTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src",
            "test", "data", "ImportCommandTest");
    private static final String TEST_CSV = "test.csv";
    private static final String TEST_AGGREGATE_CSV = "testAggregate.csv";
    private static final String TEST_DUPLICATE_INVALID_CSV = "testDuplicateInvalid.csv";
    private JsonCsvConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new JsonCsvConverter(JsonUtil.getObjectMapper(), new QueryValidator());
    }

    @Test
    public void fromCsv_validCsvFile_success() throws IOException, IllegalValueException, CommandException {
        String csvData = Files.readString(TEST_DATA_FOLDER.resolve(TEST_CSV));

        JsonSerializableAddressBook addressBook = converter.fromCsv(csvData, JsonSerializableAddressBook.class);

        assertNotNull(addressBook);
        List<Person> persons = addressBook.toModelType().getPersonList();
        assertEquals(7, persons.size());
        assertEquals("Alice Pauline", persons.get(0).getName().toString());
        assertEquals("94351253", persons.get(0).getPhone().toString());
    }

    @Test
    public void fromCsv_aggregateEntries_success() throws IOException, IllegalValueException, CommandException {
        String csvData = Files.readString(TEST_DATA_FOLDER.resolve(TEST_AGGREGATE_CSV));

        JsonSerializableAddressBook addressBook = converter.fromCsv(csvData, JsonSerializableAddressBook.class);
        addressBook = addressBook.aggregatePersons(); // Aggregate persons with same ID

        assertNotNull(addressBook);
        List<Person> persons = addressBook.toModelType().getPersonList();
        assertEquals(2, persons.size()); // Should have 2 unique persons (Alice and Benson)

        // Alice should have 3 unique anniversaries after aggregation
        Person alice = persons.stream()
                .filter(p -> p.getName().toString().equals("Alice Pauline"))
                .findFirst()
                .orElseThrow();
        assertEquals(3, alice.getAnniversaries().size());
    }

    @Test
    public void fromCsv_duplicateInvalidEntries_handlesCorrectly() throws IOException, CommandException {
        String csvData = Files.readString(TEST_DATA_FOLDER.resolve(TEST_DUPLICATE_INVALID_CSV));
        JsonSerializableAddressBook addressBook = converter.fromCsv(csvData, JsonSerializableAddressBook.class);

        // Should have both entries before processing
        assertEquals(2, addressBook.getPersons().size());

        // Test if the parsing succeeded with both entries initially
        assertTrue(addressBook.getPersons().get(0).getEmployeeId().equals(
                addressBook.getPersons().get(1).getEmployeeId()));
        // Different details for same ID
        assertTrue(!addressBook.getPersons().get(0).getName().equals(
                addressBook.getPersons().get(1).getName()));
    }

    @Test
    public void parseCsvLine_handlesEscapedQuotes() throws IOException {
        // Test parsing of CSV line with escaped quotes
        String line = "00000000-0000-0000-0000-000000000011,\"Alice \"\"The Great\"\" Pauline\","
                + "94351253,alice@example.com";
        String[] result = converter.parseCsvLine(line);

        assertEquals(4, result.length);
        assertEquals("Alice \"The Great\" Pauline", result[1]);
    }

    @Test
    public void parseCsvLine_handlesCommasInQuotes() throws IOException {
        // Test parsing of CSV line with commas inside quotes
        String line = "00000000-0000-0000-0000-000000000011,\"Pauline, Alice\",94351253,alice@example.com";
        String[] result = converter.parseCsvLine(line);

        assertEquals(4, result.length);
        assertEquals("Pauline, Alice", result[1]);
    }

    @Test
    public void toCsv_andFromCsv_maintainsData() throws IOException, IllegalValueException, CommandException {
        // Test roundtrip conversion
        String originalCsv = Files.readString(TEST_DATA_FOLDER.resolve(TEST_CSV));
        JsonSerializableAddressBook originalBook = converter.fromCsv(originalCsv, JsonSerializableAddressBook.class);

        String convertedCsv = converter.toCsv(originalBook);
        JsonSerializableAddressBook reconvertedBook = converter.fromCsv(convertedCsv,
                JsonSerializableAddressBook.class);

        assertEquals(originalBook.getPersons().size(), reconvertedBook.getPersons().size());
    }
}
