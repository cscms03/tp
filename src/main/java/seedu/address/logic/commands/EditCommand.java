package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.Data;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by a prefix of their Employee ID. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: EMPLOYEE_ID_PREFIX (there must be only one employee that matches this prefix) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_JOBPOSITION + "JOBPOSITION] "
            + "[" + PREFIX_TAG + "TAG] "
            + "[" + PREFIX_EMPLOYEEID + "EMPLOYEE ID]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_EMPLOYEE_ID_CONFLICT = "The new employee ID is either a prefix of another "
            + "existing employee ID or another existing employee ID is a prefix of this one";

    private final EmployeeId employeeIdPrefix;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param employeeIdPrefix of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(EmployeeId employeeIdPrefix, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(employeeIdPrefix);
        requireNonNull(editPersonDescriptor);

        this.employeeIdPrefix = employeeIdPrefix;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
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

        // Save the state before any potential changes
        model.commitChanges();

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (model.hasEmployeeIdPrefixConflictIgnoringSpecific(editedPerson.getEmployeeId(),
                personToEdit.getEmployeeId())) {
            throw new CommandException(MESSAGE_EMPLOYEE_ID_CONFLICT);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;
        /*
         * this is purposefully kept as personToEdit.getEmployeeId(), currently changing EmployeeID is not supported,
         * under Roman to change as he suggests.
         */
        EmployeeId employeeId = editPersonDescriptor.getEmployeeId().orElse(personToEdit.getEmployeeId());
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        JobPosition updatedjobPosition = editPersonDescriptor.getjobPosition().orElse(personToEdit.getJobPosition());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        List<Anniversary> anniversaryList = personToEdit.getAnniversaries();
        return new Person(employeeId, updatedName, updatedPhone,
                updatedEmail, updatedjobPosition, updatedTags, anniversaryList);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand otherEditCommand)) {
            return false;
        }

        return this.employeeIdPrefix.equals(otherEditCommand.employeeIdPrefix)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeIdPrefix", employeeIdPrefix)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * checks if the editCommandDescriptor has the same details, excluding EmployeeId
     * @param command the editCommand to compare with
     * @return true if they have the same details
     */
    public boolean hasSameDetails(EditCommand command) {
        requireNonNull(command);
        return editPersonDescriptor.hasSameDetails(command.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    @Data
    public static class EditPersonDescriptor {
        private EmployeeId employeeId;
        private Name name;
        private Phone phone;
        private Email email;
        private Set<Tag> tags;
        private JobPosition jobPosition;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setEmployeeId(toCopy.employeeId);
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setJobPosition(toCopy.jobPosition);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(employeeId, name, phone, email, jobPosition, tags);
        }

        public Optional<EmployeeId> getEmployeeId() {
            return Optional.ofNullable(employeeId);
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public Optional<JobPosition> getjobPosition() {
            return Optional.ofNullable(jobPosition);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("employeeId", employeeId)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("jobposition", jobPosition)
                    .add("tags", tags)
                    .toString();
        }

        /**
         * checks if the two EditPersonDescriptors have the same details, a weaker equality.
         * @param editPersonDescriptor the other descriptor
         * @return true if the same
         */
        public boolean hasSameDetails(EditPersonDescriptor editPersonDescriptor) {
            return editPersonDescriptor.employeeId.equals(this.employeeId)
                    && editPersonDescriptor.name.equals(this.name)
                    && editPersonDescriptor.phone.equals(this.phone)
                    && editPersonDescriptor.email.equals(this.email)
                    && editPersonDescriptor.jobPosition.equals(this.jobPosition)
                    && editPersonDescriptor.tags.equals(this.tags);
        }
    }
}
