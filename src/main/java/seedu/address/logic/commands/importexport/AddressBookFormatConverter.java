package seedu.address.logic.commands.importexport;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonCsvConverter;
import seedu.address.storage.JsonSerializableAddressBook;
import seedu.address.storage.QueryValidator;

/**
 * Utility for converting between different AddressBook formats (JSON, CSV).
 */
public class AddressBookFormatConverter {
    private static final Path DEFAULT_EXPORT_PATH = Paths.get("./output");
    private static final String DEFAULT_JSON_FILENAME = "output.json";
    private static final String DEFAULT_CSV_FILENAME = "output.csv";
    private static final Logger logger = LogsCenter.getLogger(AddressBookFormatConverter.class);

    // Private constructor to prevent instantiation
    private AddressBookFormatConverter() {}

    /**
     * Creates a lightweight ReadOnlyAddressBook implementation from a list of persons.
     */
    private static ReadOnlyAddressBook createTempAddressBook(ObservableList<Person> personList) {
        return () -> personList;
    }
    /**
     * Exports the displayed people to JSON format.
     */
    public static void exportToJson(ObservableList<Person> displayedPeople, Path filePath) throws IOException {
        requireNonNull(displayedPeople);
        ReadOnlyAddressBook tempAddressBook = createTempAddressBook(displayedPeople);
        JsonSerializableAddressBook jsonData = new JsonSerializableAddressBook(tempAddressBook);

        Path outputPath = (filePath != null) ? filePath : DEFAULT_EXPORT_PATH.resolve(DEFAULT_JSON_FILENAME);
        FileUtil.createParentDirsOfFile(outputPath);
        JsonUtil.saveJsonFile(jsonData, outputPath);

        String jsonString = JsonUtil.toJsonString(jsonData);
        logger.info("Exported to " + outputPath);
    }

    /**
     * Exports the displayed people to CSV format.
     */
    public static void exportToCsv(ObservableList<Person> displayedPeople, Path filePath) throws IOException {
        requireNonNull(displayedPeople);

        ReadOnlyAddressBook tempAddressBook = createTempAddressBook(displayedPeople);
        JsonSerializableAddressBook jsonData = new JsonSerializableAddressBook(tempAddressBook);

        String csvData = addressBookToCsv(jsonData);

        Path outputPath = (filePath != null) ? filePath : DEFAULT_EXPORT_PATH.resolve(DEFAULT_CSV_FILENAME);
        FileUtil.createParentDirsOfFile(outputPath);
        Files.writeString(outputPath, csvData);
        logger.info("Exported to " + outputPath);
    }
    /**
     * Imports JSON data from a file and converts it to a JsonSerializableAddressBook.
     */
    public static JsonSerializableAddressBook importFromJson(Path filePath) throws IOException, DataLoadingException {
        requireNonNull(filePath);
        return JsonUtil
                .readJsonFile(filePath, JsonSerializableAddressBook.class)
                .orElseThrow(() -> new IOException("Failed to read JSON file"));
    }

    /**
     * Imports CSV data from a file and converts it to a JsonSerializableAddressBook.
     */
    public static JsonSerializableAddressBook importFromCsv(Path filePath) throws IOException, CommandException {
        requireNonNull(filePath);
        String csvData = Files.readString(filePath);
        return csvToAddressBook(csvData);
    }
    /**
     * Converts a CSV string to a JsonSerializableAddressBook.
     */
    public static JsonSerializableAddressBook csvToAddressBook(String csvData) throws IOException, CommandException {
        requireNonNull(csvData);
        JsonCsvConverter jsonCsvConverter = new JsonCsvConverter(JsonUtil.getObjectMapper(), new QueryValidator());
        JsonSerializableAddressBook addressBook = jsonCsvConverter
                .fromCsv(csvData, JsonSerializableAddressBook.class);
        return addressBook.aggregatePersons();
    }

    /**
     * Converts a JsonSerializableAddressBook to a CSV string.
     */
    public static String addressBookToCsv(JsonSerializableAddressBook addressBook) throws IOException {
        requireNonNull(addressBook);
        JsonCsvConverter jsonCsvConverter = new JsonCsvConverter(JsonUtil.getObjectMapper(), new QueryValidator());
        return jsonCsvConverter.toCsv(addressBook);
    }
}
