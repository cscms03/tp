package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
@Data
public class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String MALFORMED_FIELD_MESSAGE_FORMAT = "Person's %s field is malformed!";
    private final String employeeId;
    private final String name;
    private final String phone;
    private final String email;
    private final String jobposition;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedAnniversary> anniversaries = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("employeeId") String employeeId,
                             @JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("job") String jobposition,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("anniversaries") List<JsonAdaptedAnniversary> anniversaries) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.jobposition = jobposition;
        this.employeeId = employeeId;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (anniversaries != null) {
            this.anniversaries.addAll(anniversaries);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        employeeId = source.getEmployeeId().toString();
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        jobposition = source.getJobPosition().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        anniversaries.addAll(source.getAnniversaries().stream()
                .map(JsonAdaptedAnniversary::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        final List<Anniversary> personAnniversaries = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        for (JsonAdaptedAnniversary anniversary : anniversaries) {
            personAnniversaries.add(anniversary.toModelType());
        }

        if (employeeId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EmployeeId.class.getSimpleName()));
        }
        EmployeeId employeeIdObj;
        try {
            employeeIdObj = EmployeeId.fromString(employeeId);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(String.format(MALFORMED_FIELD_MESSAGE_FORMAT,
                    EmployeeId.class.getSimpleName()));
        }
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (jobposition == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JobPosition.class.getSimpleName()));
        }
        if (!JobPosition.isValidJobPosition(jobposition)) {
            throw new IllegalValueException(JobPosition.MESSAGE_CONSTRAINTS);
        }
        final JobPosition modelJobPosition = new JobPosition(jobposition);
        final Set<Tag> modelTags = new HashSet<>(personTags);
        final List<Anniversary> modelAnniversaries = new ArrayList<>(personAnniversaries);
        return new Person(employeeIdObj, modelName, modelPhone,
                modelEmail, modelJobPosition, modelTags, modelAnniversaries);
    }

}
