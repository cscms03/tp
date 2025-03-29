package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.importexport.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ExportCommandParserTest {

    private static final String VALID_FILETYPE_JSON = "json";
    private static final String VALID_FILETYPE_CSV = "csv";
    private static final String VALID_FILEPATH = System.getProperty("user.home");
    private static final String VALID_FILENAME = "testexport";

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        // With all fields: filepath, filename, filetype
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + VALID_FILETYPE_JSON;

        Path expectedPath = Paths.get(VALID_FILEPATH, VALID_FILENAME + "." + VALID_FILETYPE_JSON);
        ExportCommand expectedCommand = new ExportCommand(VALID_FILETYPE_JSON, expectedPath);
        assertEquals(expectedCommand.getFiletype(), parser.parse(userInput).getFiletype());
        assertEquals(expectedCommand.getPath(), parser.parse(userInput).getPath());

        // CSV filetype
        userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME + " "
                + PREFIX_FILETYPE + VALID_FILETYPE_CSV;

        expectedPath = Paths.get(VALID_FILEPATH, VALID_FILENAME + "." + VALID_FILETYPE_CSV);
        expectedCommand = new ExportCommand(VALID_FILETYPE_CSV, expectedPath);

        assertEquals(expectedCommand.getFiletype(), parser.parse(userInput).getFiletype());
        assertEquals(expectedCommand.getPath(), parser.parse(userInput).getPath());
    }

    @Test
    public void parse_onlyTypePresent_success() throws Exception {
        String userInput = " " + PREFIX_FILETYPE + VALID_FILETYPE_JSON;
        ExportCommand expectedCommand = new ExportCommand(VALID_FILETYPE_JSON, null);

        assertEquals(expectedCommand.getFiletype(), parser.parse(userInput).getFiletype());
        assertEquals(expectedCommand.getPath(), parser.parse(userInput).getPath());
        assertEquals(expectedCommand.getPath(), null);
    }

    @Test
    public void parse_missingTypeField_throwsParseException() {
        String userInput = " " + PREFIX_FILEPATH + VALID_FILEPATH + " "
                + PREFIX_FILENAME + VALID_FILENAME;

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidFileType_throwsParseException() {
        String userInput = " " + PREFIX_FILETYPE + "pdf"; // Unsupported type

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
