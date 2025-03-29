package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_REMINDER_TYPE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Filters and displays a list of employees whose birthdays or work anniversaries
 * are occurring within the next 3 days.
 */
public class ReminderCommand extends Command {
    public static final String COMMAND_WORD = "reminder";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters a list of people with upcoming birthdays or work anniversaries within 3 days.\n"
            + "Parameters: TYPE (must be either 'bd' for birthday or 'wa' for work anniversary)\n"
            + "Example: " + COMMAND_WORD + " bd";

    public static final String MESSAGE_SUCCESS_BIRTHDAY = "Displayed employees with upcoming birthdays!";
    public static final String MESSAGE_SUCCESS_WORK_ANNIVERSARY =
            "Displayed employees with upcoming work anniversaries!";

    private final String type;

    /**
     * Creates a ReminderCommand to filter either birthdays or work anniversaries.
     *
     * @param type The reminder type: "bd" for birthdays or "wa" for work anniversaries.
     */
    public ReminderCommand(String type) {
        this.type = type;
    }

    /**
     * Executes the reminder command.
     * Filters the reminder list based on the specified type (birthday or work anniversary).
     *
     * @param model The model which holds the address book data and filtered lists.
     * @return A CommandResult indicating success and displaying the filtered reminder list.
     * @throws CommandException If the type provided is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        return switch (type) {
        case "bd" -> {
            model.updateBirthdayReminderList();
            yield new CommandResult(MESSAGE_SUCCESS_BIRTHDAY);
        }
        case "wa" -> {
            model.updateWorkAnniversaryReminderList();
            yield new CommandResult(MESSAGE_SUCCESS_WORK_ANNIVERSARY);
        }
        default -> throw new CommandException(MESSAGE_INVALID_REMINDER_TYPE);
        };
    }
}
