package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A person is considered unique by comparing using {@code Person#isSamePerson(Person)}. As such, adding and updating of
 * persons uses Person#isSamePerson(Person) for equality so as to ensure that the person being added or updated is
 * unique in terms of identity in the UniquePersonList. However, the removal of a person uses Person#equals(Object) so
 * as to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    private final ObservableList<Person> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Sorts the list of persons by their next upcoming date (birthday or work anniversary).
     * - Persons with the nearest upcoming date will appear first.
     * - Persons without a birthday or anniversary will be placed at the end of the list.
     */
    public void sortByUpcomingDate() {
        List<Person> sortedList = internalList.stream()
                .sorted(Comparator
                        .comparing(Person::getNextUpcomingDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                )
                .collect(Collectors.toList());

        // Update the list after sorting
        internalList.setAll(sortedList);
    }
    /**
     * Calculates the number of days until the next occurrence of a birthday.
     *
     * @param birthday The date of the birthday.
     * @return Number of days until the next birthday, or {@code null} if the birthday is {@code null}.
     */
    private Integer daysUntilNextBirthday(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate nextBirthday = birthday.withYear(today.getYear());

        // If the birthday has already passed this year, adjust to next year
        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        // Return the number of days until the next birthday
        return (int) ChronoUnit.DAYS.between(today, nextBirthday);
    }

    /**
     * Adds a person to the list.
     * The person must not already exist in the list.
     */
    public void add(Person toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the list.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedPerson) && contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent person from the list.
     * The person must exist in the list.
     */
    public void remove(Person toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        requireAllNonNull(persons);
        if (!personsAreUnique(persons)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(persons);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniquePersonList)) {
            return false;
        }

        UniquePersonList otherUniquePersonList = (UniquePersonList) other;
        return internalList.equals(otherUniquePersonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code persons} contains only unique persons.
     */
    private boolean personsAreUnique(List<Person> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePerson(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if there exist a person specifically duplicated
     * @param toCheck the person to check
     * @return true if there exists more than one person that are duplicated
     */
    public boolean hasDuplicatePersonDetails(Person toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(checkPerson -> checkPerson.hasSameDetails(toCheck));
    }
    /**
     * Checks if there is an employee ID in the list that has a prefix conflict
     * with the given employee ID. A prefix conflict occurs when one employee ID
     * is a prefix of another one.
     *
     * @param employeeId the employee ID to check for conflicts.
     *                   Must not be null.
     * @return true if a prefix conflict is found, false otherwise.
     */
    public boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId) {
        requireNonNull(employeeId);
        return internalList.stream().anyMatch(person -> person.getEmployeeId().hasPrefixConflict(employeeId));
    }

    /**
     * Checks if there is an employee ID in the list that has a prefix conflict
     * with the given employee ID, while ignoring a specific employee ID.
     * A prefix conflict occurs when one employee ID is a prefix of another.
     *
     * @param employeeId the employee ID to check for conflicts.
     * @param toIgnore   the employee ID to be ignored during the check.
     * @return true if a prefix conflict is found excluding the specified employee ID to ignore, false otherwise.
     */
    public boolean hasEmployeeIdPrefixConflictIgnoringSpecific(EmployeeId employeeId, EmployeeId toIgnore) {
        requireNonNull(employeeId);
        requireNonNull(toIgnore);
        return internalList.stream()
                .filter(person -> !person.getEmployeeId().equals(toIgnore))
                .anyMatch(person -> person.getEmployeeId().hasPrefixConflict(employeeId));
    }


}
