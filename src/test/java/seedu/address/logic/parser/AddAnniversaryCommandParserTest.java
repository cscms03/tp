package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.AnniversaryParserUtils.INVALID_ANNIVERSARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.anniversary.AddAnniversaryCommand;
import seedu.address.logic.parser.anniversary.AddAnniversaryCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;

public class AddAnniversaryCommandParserTest {
    private static final String PREFIX_EID_PARSABLE = " " + PREFIX_EMPLOYEEID;
    private AddAnniversaryCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new AddAnniversaryCommandParser();
    }

    @Test
    public void parse_validInput_returnsAddAnniversaryCommand() throws Exception {
        String userInput = PREFIX_EID_PARSABLE + "00000000-0000-0000-0000-000000000001 "
                + PREFIX_ANNIVERSARY_DATE + "2025-03-13 "
                + PREFIX_ANNIVERSARY_NAME + "BirthdayName "
                + PREFIX_ANNIVERSARY_TYPE + "Personal "
                + PREFIX_ANNIVERSARY_TYPE_DESC + "Family "
                + PREFIX_ANNIVERSARY_DESC + "Sample Description";
        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();
        assertEquals("BirthdayName", anniv.getName());
        assertEquals("Sample Description", anniv.getDescription());
    }

    @Test
    public void parse_missingEmployeeId_throwsParseException() {
        String userInput = " " + PREFIX_ANNIVERSARY_DATE + "2025-03-13 "
                + PREFIX_NAME + "MissingEid "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_NAME + "NoDate "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingName_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "2025-01-01 "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "invaliddate "
                + PREFIX_NAME + "InvalidDate "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingAnniversaryType_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "2025-02-25 "
                + PREFIX_NAME + "NoType ";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_tooManyAnniversaryType_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "2025-03-25 "
                + PREFIX_NAME + "TooManyTypes "
                + PREFIX_ANNIVERSARY_TYPE + "One "
                + PREFIX_ANNIVERSARY_TYPE + "Two "
                + PREFIX_ANNIVERSARY_TYPE + "Three";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, INVALID_ANNIVERSARY),
                ex.getMessage());
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        String userInput = "some_invalid_input";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAnniversaryCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_invalidAnniversaryDateWithSpaces_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "invalid_date "
                + PREFIX_NAME + "AnnivName "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
