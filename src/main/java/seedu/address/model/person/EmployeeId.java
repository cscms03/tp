package seedu.address.model.person;

/**
 * Represents a Person's employee ID in the address book
 * Person ID is used as a universal static identifier,
 * serving as a reference interface.
 * At any point in time, for two different employees
 * no employee id of one employee shall be prefix of the the employee id
 * of another employee.
 */
public class EmployeeId {

    public static final String MESSAGE_CONSTRAINTS =
            "Employee ID must be 1-36 characters long, containing only letters, digits, and '-'.";

    public static final String MESSAGE_PREFIX_CONSTRAINTS =
            "Employee ID prefix must be 1-36 characters long, containing only letters, digits, and '-'.";

    public static final String VALIDATION_REGEX = "[a-zA-Z0-9-]{1,36}";

    public final String value;

    /**
     * Constructs an {@code EmployeeId}.
     *
     * @param employeeId A valid employee ID.
     */
    public EmployeeId(String employeeId) {
        this.value = employeeId;
    }

    /**
     * Generates an employee ID from a String
     */
    public static EmployeeId fromString(String employeeId) {
        return new EmployeeId(employeeId);
    }

    /**
     * Generates a new employee ID as a random UUID.
     */
    public static EmployeeId generateNewEmployeeId() {
        return new EmployeeId(java.util.UUID.randomUUID().toString());
    }

    /**
     * Returns true if a given string is a valid employee ID.
     */
    public static boolean isValidEmployeeId(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Checks if this EmployeeId is a prefix of another EmployeeId.
     *
     * @param other The EmployeeId to be checked against.
     * @return true if this EmployeeId is a prefix of the specified EmployeeId, otherwise false.
     */
    public boolean isPrefixOf(EmployeeId other) {
        return other.value.startsWith(this.value);
    }

    /**
     * Returns true if the employee ID has a prefix conflict with another employee ID.
     * A prefix conflict occurs when one employee ID is a prefix of another one.
     */
    public boolean hasPrefixConflict(EmployeeId other) {
        return this.isPrefixOf(other) || other.isPrefixOf(this);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EmployeeId)) {
            return false;
        }

        EmployeeId otherEmployeeId = (EmployeeId) other;
        return value.equals(otherEmployeeId.value);
    }
}
