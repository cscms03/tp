package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.AnniversaryParserUtils.MESSAGE_DATE_CONSTRAINTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.anniversary.Birthday;
import seedu.address.model.anniversary.WorkAnniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_EMPLOYEE_ID_PREFIX_NOT_SPECIFIED = "Employee id prefix not specified!";
    public static final String MESSAGE_EMPLOYEE_ID_PREFIX_FORMAT = "Employee id can't start contain spaces!";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String employeeId} into a {@code UUID}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static EmployeeId parseEmployeeId(String employeeId) throws ParseException {
        requireNonNull(employeeId);
        String trimmedEmployeeId = employeeId.trim();
        if (!EmployeeId.isValidEmployeeId(trimmedEmployeeId)) {
            throw new ParseException(EmployeeId.MESSAGE_CONSTRAINTS);
        }
        return EmployeeId.fromString(trimmedEmployeeId);
    }

    /**
     * Parses a {@code String employeeIdPrefix} into a {@code String}.
     *
     * @throws ParseException if the given {@code employeeIdPrefix} is empty or contains spaces.
     */
    public static EmployeeId parseEmployeeIdPrefix(String employeeIdPrefix) throws ParseException {
        requireNonNull(employeeIdPrefix);
        if (employeeIdPrefix.isEmpty()) {
            throw new ParseException(MESSAGE_EMPLOYEE_ID_PREFIX_NOT_SPECIFIED);
        }
        employeeIdPrefix = employeeIdPrefix.trim();
        if (!EmployeeId.isValidEmployeeId(employeeIdPrefix)) {
            throw new ParseException(EmployeeId.MESSAGE_PREFIX_CONSTRAINTS);
        }
        return EmployeeId.fromString(employeeIdPrefix);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static JobPosition parseJobPosition(String jobposition) throws ParseException {
        requireNonNull(jobposition);
        String trimmedjobposition = jobposition.trim();
        if (!JobPosition.isValidJobPosition(trimmedjobposition)) {
            throw new ParseException(JobPosition.MESSAGE_CONSTRAINTS);
        }
        return new JobPosition(trimmedjobposition);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String name}, {@code String dateStr}, and a {@code String type} into an {@code Anniversary}.
     *
     * @param name the name of the anniversary for custom anniversaries
     * @param description the description of the anniversary
     * @param dateStr the date of the anniversary
     * @param type the type of the anniversary
     * @throws ParseException if the given {@code dateStr} is invalid.
     */
    public static Anniversary parseAnniversary(String name, String description, String dateStr, String type,
                                               String typeDescription) throws ParseException {
        requireNonNull(dateStr);
        String trimmedAnniversaryDate = dateStr.trim();
        LocalDate date;
        try {
            date = LocalDate.parse(trimmedAnniversaryDate);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_DATE_CONSTRAINTS);
        }
        if (name == null || name.isEmpty()) {
            return new Anniversary(date, new AnniversaryType(type, typeDescription),
                    description, type);
        } else {
            return new Anniversary(date, new AnniversaryType(type, typeDescription),
                    description, name);
        }
    }

    /**
     * Parses a {@code String name}, {@code String dateStr}, and a {@code String type} into an {@code Anniversary}.
     *
     * @param name the name of the person attributed to prebuilt-anniversaries
     * @param dateStr the date of the anniversary
     * @param type the prefix of the anniversary
     * @throws ParseException if the given {@code dateStr} is invalid.
     */
    public static Anniversary parseAnniversaryWithName(Name name, String dateStr,
                                                       Prefix type) throws ParseException {
        String trimmedAnniversaryDate = dateStr.trim();
        LocalDate date;
        try {
            date = LocalDate.parse(trimmedAnniversaryDate);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_DATE_CONSTRAINTS);
        }
        if (type.equals(PREFIX_BIRTHDAY)) {
            String birthdayAppend = "Birthday";
            return new Anniversary(date, new Birthday(), name + "'s " + birthdayAppend, birthdayAppend);
        }
        if (type.equals(PREFIX_WORK_ANNIVERSARY)) {
            String workAnniversaryAppend = "work anniversary";
            return new Anniversary(date, new WorkAnniversary(), name + "'s "
                    + workAnniversaryAppend, workAnniversaryAppend);
        }
        throw new ParseException("Invalid anniversary type");
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
