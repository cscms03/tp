package seedu.address.logic.parser;


import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ReminderCommand.MESSAGE_USAGE;

import seedu.address.logic.commands.ReminderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ReminderCommand object.
 */
public class ReminderCommandParser implements Parser<ReminderCommand> {

    /**
     * Parses the given {@code String} of arguments and returns a ReminderCommand object.
     *
     * @throws ParseException if the user input does not conform to expected format
     */
    public ReminderCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim().toLowerCase();

        if (!trimmedArgs.equals("bd") && !trimmedArgs.equals("wa")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        return new ReminderCommand(trimmedArgs);
    }
}

