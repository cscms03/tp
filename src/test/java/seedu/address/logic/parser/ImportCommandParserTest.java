package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WRITE_MODE;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.importexport.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ImportCommandParserTest {

    private static final String VALID_FILETYPE_JSON = "json";
    private static final String VALID_FILETYPE_CSV = "csv";
    private static final String VALID_FILEPATH = System.getProperty("user.home");
    private static final String VALID_FILENAME = "testimport";
    private static final String VALID_WRITE_MODE_APPEND = "append";
    private static final String VALID_WRITE_MODE_OVERWRITE = "overwrite";

    private final ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        // With all fields and append mode
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + VALID_FILETYPE_JSON + " "
                + PREFIX_WRITE_MODE + VALID_WRITE_MODE_APPEND;

        Path expectedPath = Paths.get(VALID_FILEPATH, VALID_FILENAME + "." + VALID_FILETYPE_JSON);
        ImportCommand expectedCommand = new ImportCommand(
                VALID_FILETYPE_JSON, expectedPath, VALID_WRITE_MODE_APPEND);

        assertEquals(expectedCommand.getFiletype(), parser.parse(userInput).getFiletype());
        assertEquals(expectedCommand.getPath(), parser.parse(userInput).getPath());

        // With all fields and overwrite mode
        userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + VALID_FILETYPE_CSV + " "
                + PREFIX_WRITE_MODE + VALID_WRITE_MODE_OVERWRITE;

        expectedPath = Paths.get(VALID_FILEPATH, VALID_FILENAME + "." + VALID_FILETYPE_CSV);
        expectedCommand = new ImportCommand(
                VALID_FILETYPE_CSV, expectedPath, VALID_WRITE_MODE_OVERWRITE);

        assertEquals(expectedCommand.getFiletype(), parser.parse(userInput).getFiletype());
        assertEquals(expectedCommand.getPath(), parser.parse(userInput).getPath());
    }

    @Test
    public void parse_missingWriteMode_throwsParseException() {
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + VALID_FILETYPE_JSON;

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingFileType_throwsParseException() {
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_WRITE_MODE + VALID_WRITE_MODE_APPEND;

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidFileType_throwsParseException() {
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + "pdf" + " " // Unsupported type
                + PREFIX_WRITE_MODE + VALID_WRITE_MODE_APPEND;

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidWriteMode_throwsParseException() {
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + VALID_FILETYPE_JSON + " "
                + PREFIX_WRITE_MODE + "merge"; // Invalid write mode

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
