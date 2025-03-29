package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMPLOYEE_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMPLOYEE_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOBPOSITION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOBPOSITION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORK_ANNIVERSARY_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORK_ANNIVERSARY_DATE_BOB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersonsWithAnniversaries {

    public static final Person ALICE = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000011")
            .withName("Alice Pauline")
            .withJobPosition("Hiring Womanager")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1995, 3, 16), LocalDate.of(2017, 8, 7))
            .build();
    public static final Person BENSON = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000012")
            .withName("Benson Meier")
            .withJobPosition("Financial Advisor")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1991, 5, 11), LocalDate.of(2015, 12, 25))
            .build();
    public static final Person CARL = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000003")
            .withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com")
            .withJobPosition("Wall mart")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1997, 1, 1), LocalDate.of(2018, 8, 18))
            .build();
    public static final Person DANIEL = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000004")
            .withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withJobPosition("10th man")
            .withTags("friends")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1993, 11, 13), LocalDate.of(2019, 2, 27))
            .build();
    public static final Person ELLE = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000005")
            .withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com")
            .withJobPosition("The one")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1992, 9, 15), LocalDate.of(2016, 6, 1))
            .build();
    public static final Person FIONA = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000006")
            .withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com")
            .withJobPosition("little dyke")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1994, 8, 22), LocalDate.of(2017, 11, 22))
            .build();
    public static final Person GEORGE = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000007")
            .withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com")
            .withJobPosition("4th man")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1996, 4, 12), LocalDate.of(2018, 10, 12))
            .build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withEmployeeId("00000000-0000-0000-0000-000000000014")
            .withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com")
            .withJobPosition("little guy")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1999, 1, 11), LocalDate.of(2020, 1, 1))
            .build();
    public static final Person IDA = new PersonBuilder().withEmployeeId("00000000-0000-0000-0000-000000000013")
            .withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com")
            .withJobPosition("chicago man")
            .withBirthdayAndWorkAnniversary(LocalDate.of(1998, 5, 5), LocalDate.of(2019, 5, 5))
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withEmployeeId(VALID_EMPLOYEE_ID_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withJobPosition(VALID_JOBPOSITION_AMY).withTags(VALID_TAG_FRIEND)
            .withBirthdayAndWorkAnniversary(VALID_BIRTHDAY_DATE_AMY, VALID_WORK_ANNIVERSARY_DATE_AMY)
            .build();
    public static final Person BOB = new PersonBuilder().withEmployeeId(VALID_EMPLOYEE_ID_BOB)
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withJobPosition(VALID_JOBPOSITION_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .withBirthdayAndWorkAnniversary(VALID_BIRTHDAY_DATE_BOB, VALID_WORK_ANNIVERSARY_DATE_BOB)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersonsWithAnniversaries() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
