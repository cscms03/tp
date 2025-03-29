package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS;

import java.util.List;

import lombok.Getter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;

/**
 * Deletes Anniversaries from a person
 */
@Getter
public class DeleteAnniversaryCommand extends Command {
    public static final String MESSAGE_SUCCESS = "anniversary deleted: %1$s";
    public static final String COMMAND_WORD = "deleteAnni";
    public static final Object MESSAGE_USAGE = COMMAND_WORD + ": deletes an anniversary to the person identified by a "
            + "prefix of their Employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID "
            + "ad/index ";
    private final Index targetIndex;
    private final EmployeeId employeeIdPrefix;

    /**
     * constructs a deleteAnniversaryCommand
     * @param targetIndex tar
     * @param employeeIdPrefix emp
     */
    public DeleteAnniversaryCommand(Index targetIndex, EmployeeId employeeIdPrefix) {
        this.targetIndex = targetIndex;
        this.employeeIdPrefix = employeeIdPrefix;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> matchedEmployees = model.getFilteredByEmployeeIdPrefixList(employeeIdPrefix);

        if (matchedEmployees.size() > 1) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX,
                    employeeIdPrefix
            ));
        }

        if (matchedEmployees.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_PERSON_PREFIX_NOT_FOUND,
                    employeeIdPrefix
            ));
        }

        Person personToEdit = matchedEmployees.get(0);
        List<Anniversary> anniversaryList = personToEdit.getAnniversaries();


        if (targetIndex.getZeroBased() >= anniversaryList.size()) {
            throw new CommandException(MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS);
        }

        Anniversary anniversaryToDelete = anniversaryList.get(targetIndex.getZeroBased());
        anniversaryList.remove(anniversaryToDelete);
        Person updatedPerson = Person.builder()
                .employeeId(personToEdit.getEmployeeId())
                .name(personToEdit.getName())
                .jobPosition(personToEdit.getJobPosition())
                .email(personToEdit.getEmail())
                .phone(personToEdit.getPhone())
                .tags(personToEdit.getTags())
                .anniversaries(anniversaryList).build();
        // update the model
        model.setPerson(personToEdit, updatedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, anniversaryToDelete));
    }
}
