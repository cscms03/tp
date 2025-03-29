package seedu.address.testutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.anniversary.Birthday;
import seedu.address.model.anniversary.WorkAnniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
@Builder
public class PersonBuilder {

    public static final String DEFAULT_EMPLOYEE_ID = "00000000-0000-0000-0000-000000000001";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_JOBPOSITION = "Hiring Manager";
    public static final Anniversary DEFAULT_BIRTHDAY = new Anniversary(LocalDate.of(2000, 1, 1),
            new Birthday(), "Birthday", "Amy");
    public static final Anniversary DEFAULT_WORK_ANNIVERSARY = new Anniversary(LocalDate.of(2000, 1, 1),
            new WorkAnniversary(), "Work Anniversary", "Amy");

    private EmployeeId employeeId;
    private Name name;
    private Phone phone;
    private Email email;
    private JobPosition jobPosition;
    private Set<Tag> tags;
    private List<Anniversary> anniversaries;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        employeeId = EmployeeId.fromString(DEFAULT_EMPLOYEE_ID);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        jobPosition = new JobPosition(DEFAULT_JOBPOSITION);
        tags = new HashSet<>();
        anniversaries = new ArrayList<>();
    }
    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        employeeId = personToCopy.getEmployeeId();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        jobPosition = personToCopy.getJobPosition();
        tags = new HashSet<>(personToCopy.getTags());
        anniversaries = new ArrayList<>(personToCopy.getAnniversaries());
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     * @return
     */
    public static Person defaultPerson() {
        Person person = new PersonBuilder()
                .withEmployeeId(DEFAULT_EMPLOYEE_ID)
                .withName(DEFAULT_NAME)
                .withEmail(DEFAULT_EMAIL)
                .withPhone(DEFAULT_PHONE)
                .withJobPosition(DEFAULT_JOBPOSITION)
                .build();
        person.getAnniversaries().add(DEFAULT_BIRTHDAY);
        person.getAnniversaries().add(DEFAULT_WORK_ANNIVERSARY);
        person.getTags();
        return person;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withJobPosition(String jobposition) {
        this.jobPosition = new JobPosition(jobposition);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code EmployeeID} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmployeeId(String employeeId) {
        this.employeeId = EmployeeId.fromString(employeeId);
        return this;
    }

    /**
     * Sets both the Birthday and Work Anniversary for the {@code Person} that we are building.
     */
    public PersonBuilder withBirthdayAndWorkAnniversary(LocalDate localBDate, LocalDate localWaDate) {
        List<Anniversary> anni = new ArrayList<>();
        anni.add(new Anniversary(localBDate, new Birthday(),
                "Birthday", name.fullName));
        anni.add(new Anniversary(localWaDate, new WorkAnniversary(),
                "Work Anniversary", name.fullName));
        this.anniversaries = anni;
        return this;
    }
    /**
     * Adds an anniversary to the person's anniversary list.
     */
    public PersonBuilder withAnniversary(String date, String type, String name, String description) {
        this.anniversaries.add(new Anniversary(
                LocalDate.parse(date),
                new AnniversaryType(type, ""),
                name, description));
        return this;
    }

    public Person build() {
        return new Person(employeeId, name, phone, email, jobPosition, tags, anniversaries);
    }

}
