package seedu.address.logic.commands.importexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ImportCommandIntegrationTest {

    public static final Path TEST_DATA_FOLDER = Paths.get("src",
            "test", "data", "ImportCommandTest");
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_importValidCsv_success() throws Exception {
        Path testCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        ImportCommand importCommand = new ImportCommand("csv", testCsvPath, "overwrite");

        CommandResult commandResult = importCommand.execute(model);

        assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_OVERWRITE, 7), commandResult.getFeedbackToUser());
        assertEquals(7, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_importAggregateCsv_success() throws Exception {
        // First import base data
        Path testCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        ImportCommand importCommand = new ImportCommand("csv", testCsvPath, "overwrite");
        importCommand.execute(model);

        // Then import aggregate data in append mode
        Path aggregateCsvPath = TEST_DATA_FOLDER.resolve("testAggregate.csv");
        ImportCommand appendCommand = new ImportCommand("csv", aggregateCsvPath, "append");

        CommandResult commandResult = appendCommand.execute(model);

        // Should have imported 2 person merged from the aggregate CSV
        assertTrue(commandResult.getFeedbackToUser().contains("Successfully imported"));
        assertFalse(commandResult.getFeedbackToUser().contains("Alice Pauline"));
    }

    @Test
    public void execute_importDuplicateInvalidCsv_handlesConflicts() throws Exception {
        // First import base data with Alice
        Path testCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        ImportCommand importCommand = new ImportCommand("csv", testCsvPath, "overwrite");
        importCommand.execute(model);

        // Then import data with conflicting Alice records
        Path duplicatePath = TEST_DATA_FOLDER.resolve("testDuplicateInvalid.csv");
        ImportCommand appendCommand = new ImportCommand("csv", duplicatePath, "append");
        CommandResult commandResult = appendCommand.execute(model);

        // Should mention skipped contacts due to conflicts
        assertTrue(commandResult.getFeedbackToUser().contains("Alice Pauline"));
        assertTrue(commandResult.getFeedbackToUser().contains("Alice Paulina"));
    }

    @Test
    public void execute_importDuplicateInvalidCsvOverwrite_throwsException() throws Exception {
        // Then import data with conflicting Alice records
        Path duplicatePath = TEST_DATA_FOLDER.resolve("testDuplicateInvalid.csv");
        ImportCommand appendCommand = new ImportCommand("csv", duplicatePath, "overwrite");
        assertThrows(CommandException.class, () ->appendCommand.execute(model));
    }

    @Test
    public void execute_importInvalidData_throwsCommandException() {
        Path invalidPath = Paths.get("nonexistent.csv");
        ImportCommand importCommand = new ImportCommand("csv", invalidPath, "overwrite");

        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void execute_importEmptyCsv_throwsCommandException() {
        // Trying to import an empty CSV file
        Path emptyCsvPath = TEST_DATA_FOLDER.resolve("empty.csv");
        ImportCommand importCommand = new ImportCommand("csv", emptyCsvPath, "append");
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void execute_importJsonOverwrite_success() throws Exception {
        // Overwriting with valid JSON data
        Path testJsonPath = TEST_DATA_FOLDER.resolve("test.json");
        ImportCommand importCommand = new ImportCommand("json", testJsonPath, "overwrite");
        CommandResult commandResult = importCommand.execute(model);

        // Check some expected feedback (adjust the number if required by your JSON)
        assertTrue(commandResult.getFeedbackToUser().contains("Successfully imported"));
    }

    @Test
    public void execute_importCsvWithMissingHeaders_throwsCommandException() {
        // CSV missing one or more required headers
        Path missingHeadersCsvPath = TEST_DATA_FOLDER.resolve("testMissingHeaders.csv");
        ImportCommand importCommand = new ImportCommand("csv", missingHeadersCsvPath, "append");
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void execute_importCsvMultipleConflicts_multiImport() throws Exception {
        // This CSV has multiple persons with conflicting EmployeeIds
        Path noConflictJsonPath = TEST_DATA_FOLDER.resolve("test.json");
        ImportCommand importCommand = new ImportCommand("json", noConflictJsonPath, "overwrite");
        CommandResult commandResult = importCommand.execute(model);

        // Ensures partial import still succeeds while reporting conflicts
        assertTrue(commandResult.getFeedbackToUser().contains("Successfully imported"));

        // This CSV has multiple persons with conflicting EmployeeIds
        Path noConflictCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        importCommand = new ImportCommand("csv", noConflictCsvPath, "append");
        commandResult = importCommand.execute(model);

        // Ensures partial import still succeeds while reporting conflicts
        assertTrue(commandResult.getFeedbackToUser().contains("Successfully imported"));
    }
}
