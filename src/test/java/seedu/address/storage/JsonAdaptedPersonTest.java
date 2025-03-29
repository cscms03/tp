package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_EMPLOYEEID = "@";
    private static final String INVALID_ANNIVERSARY = "@";
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_JOBPOSITION = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_EMPLOYEE_ID = BENSON.getEmployeeId().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_JOBPOSITION = BENSON.getJobPosition().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedAnniversary> VALID_ANNIVERSARY = BENSON.getAnniversaries()
            .stream()
            .map(JsonAdaptedAnniversary::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_EMPLOYEE_ID,
                        INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_JOBPOSITION,
                        VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_EMPLOYEE_ID, null, VALID_PHONE, VALID_EMAIL,
                VALID_JOBPOSITION, VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                        VALID_JOBPOSITION, VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME, null, VALID_EMAIL,
                VALID_JOBPOSITION, VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                        VALID_JOBPOSITION, VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME, VALID_PHONE, null,
                VALID_JOBPOSITION, VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidJobPosition_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        INVALID_JOBPOSITION, VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = JobPosition.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullJobPosition_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME,
                VALID_PHONE, VALID_EMAIL, null,
                VALID_TAGS, VALID_ANNIVERSARY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, JobPosition.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_EMPLOYEE_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_JOBPOSITION, invalidTags, VALID_ANNIVERSARY);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
