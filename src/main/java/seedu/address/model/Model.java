package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);
    /**
     * Returns true if more than one person with the same identity as {@code person} exists in the address book.
     */
    boolean hasDuplicatePersonDetails(Person person);
    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Returns true if the given employeeId has a prefix conflict with any existing employeeId in the address book.
     * Prefix conflict is defined as having the same prefix as another employeeId in the address book.
     */
    boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId);

    /**
     * Checks if the given employee ID has a prefix conflict with any existing employee ID in the address book,
     * excluding the specified {@code toIgnore} employee ID.
     * A prefix conflict occurs when one employee ID is a prefix of another.
     *
     * @param employeeId The employee ID to check for prefix conflict.
     * @param toIgnore The employee ID to ignore while checking for prefix conflicts.
     * @return True if a prefix conflict exists, excluding the specified {@code toIgnore} employee ID; false otherwise.
     */
    boolean hasEmployeeIdPrefixConflictIgnoringSpecific(EmployeeId employeeId, EmployeeId toIgnore);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns an unmodifiable view of the filtered person list that contains only employees with id starting with
     * the provided one
     */
    ObservableList<Person> getFilteredByEmployeeIdPrefixList(EmployeeId employeeIdPrefix);

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    ObservableList<Person> getBirthdayReminderList();

    ObservableList<Person> getWorkAnniversaryReminderList();

    void commitChanges();

    /**
     * Updates the list of persons who have an upcoming birthday within the next N days.
     */
    void updateBirthdayReminderList();

    /**
     * Updates the list of persons who have an upcoming work anniversary within the next N days.
     */
    void updateWorkAnniversaryReminderList();
}
