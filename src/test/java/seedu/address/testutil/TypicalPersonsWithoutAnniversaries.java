package seedu.address.testutil;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersonsWithoutAnniversaries {

    public static final Person ALICE = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000011")
            .withName("Alice Pauline")
            .withJobPosition("Hiring Womanager")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends")
            .build();
    public static final Person BENSON = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000012")
            .withName("Benson Meier")
            .withJobPosition("Financial Advisor")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends")
            .build();
    public static final Person CARL = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000003")
            .withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com")
            .withJobPosition("Wall mart")
            .build();
    public static final Person DANIEL = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000004")
            .withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withJobPosition("10th man")
            .withTags("friends")
            .build();
    public static final Person ELLE = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000005")
            .withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com")
            .withJobPosition("The one")
            .build();
    public static final Person FIONA = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000006")
            .withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com")
            .withJobPosition("little dyke")
            .build();
    public static final Person GEORGE = new PersonBuilder()
            .withEmployeeId("00000000-0000-0000-0000-000000000007")
            .withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com")
            .withJobPosition("4th man")
            .build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withEmployeeId("00000000-0000-0000-0000-000000000014")
            .withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com")
            .withJobPosition("little guy")
            .build();
    public static final Person IDA = new PersonBuilder().withEmployeeId("00000000-0000-0000-0000-000000000013")
            .withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com")
            .withJobPosition("chicago man")
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withEmployeeId(VALID_EMPLOYEE_ID_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withJobPosition(VALID_JOBPOSITION_AMY).withTags(VALID_TAG_FRIEND)
            .build();
    public static final Person BOB = new PersonBuilder().withEmployeeId(VALID_EMPLOYEE_ID_BOB)
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withJobPosition(VALID_JOBPOSITION_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersonsWithoutAnniversaries() {} // prevents instantiation

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
