package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_EMPTY_FIELD_WITH_PREFIX = "At least one non-empty field is required.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    public static final String MESSAGE_SUCCESS = "New anniversary added: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with employeeId = %s";
    public static final String MESSAGE_PERSON_PREFIX_NOT_FOUND = "No person found with employeeId starting with %s";
    public static final String MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX =
            "Found multiple employees with employeeId starting with %s";
    public static final String MESSAGE_DUPLICATE_ANNIVERSARY =
            "This exact anniversary (date + name + type + description) already exists for that person.";
    public static final String MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS =
            "The index you are searching for is out of bounds for the anniversary.";
    public static final String MESSAGE_INVALID_REMINDER_TYPE =
            "Invalid reminder type! Please use 'bd' for birthday or 'wa' for work anniversary.";



    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Job: ")
                .append(person.getJobPosition())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
