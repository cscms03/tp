package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.function.Predicate;

import lombok.Getter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
@Getter
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Finds all persons whose names or job position contain any of "
                    + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
                    + "If both name and job position keywords are provided, "
                    + "then only employees with both matching name and job position will be listed.\n"
                    + "Parameters for Name search: " + PREFIX_NAME + "KEYWORD [MORE_KEYWORDS]...\n"
                    + "Parameters for Position search: " + PREFIX_JOBPOSITION + "KEYWORD [MORE_KEYWORDS]...\n"
                    + "Parameters for Combined search: " + PREFIX_NAME + "KEYWORD [MORE_KEYWORDS]... "
                    + PREFIX_JOBPOSITION + "KEYWORD [MORE_KEYWORDS]...\n"
                    + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + " alice bob charlie";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
