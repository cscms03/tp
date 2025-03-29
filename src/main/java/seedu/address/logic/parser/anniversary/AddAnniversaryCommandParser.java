package seedu.address.logic.parser.anniversary;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;

import seedu.address.logic.commands.anniversary.AddAnniversaryCommand;
import seedu.address.logic.parser.AnniversaryParserUtils;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.EmployeeId;

/**
 * Parses input arguments and creates a new AddAnniversaryCommand object
 */
public class AddAnniversaryCommandParser implements Parser<AddAnniversaryCommand> {

    @Override
    public AddAnniversaryCommand parse(String args) throws ParseException {
        // tokenize using relevant prefixes
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_EMPLOYEEID, PREFIX_ANNIVERSARY_DATE, PREFIX_ANNIVERSARY_NAME,
                PREFIX_ANNIVERSARY_DESC, PREFIX_ANNIVERSARY_TYPE, PREFIX_ANNIVERSARY_TYPE_DESC,
                PREFIX_BIRTHDAY, PREFIX_WORK_ANNIVERSARY, PREFIX_NAME
        );

        // Basic validation to ensure required prefixes exist
        if (argMultimap.getValue(PREFIX_EMPLOYEEID).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAnniversaryCommand.MESSAGE_USAGE)
            );
        }
        // parse required fields
        EmployeeId employeeIdPrefix = ParserUtil.parseEmployeeIdPrefix(argMultimap.getValue(PREFIX_EMPLOYEEID).get());
        try {
            Anniversary anniversary = AnniversaryParserUtils.parseAnniversary(argMultimap);
            return new AddAnniversaryCommand(employeeIdPrefix, anniversary);
        } catch (ParseException pe) {
            throw pe;
        }
    }
}
