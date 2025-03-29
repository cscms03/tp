package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
@Getter
public class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
        return addressBook;
    }

    /**
     * Aggregates persons with the same name and merges their anniversaries.
     * @return a new JsonSerializableAddressBook with aggregated persons
     */
    public JsonSerializableAddressBook aggregatePersons() {
        Map<PersonKey, List<JsonAdaptedAnniversary>> groupedAnniversaries = new HashMap<>();

        for (JsonAdaptedPerson person : persons) {
            PersonKey key = PersonKey.from(person);
            groupedAnniversaries
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .addAll(person.getAnniversaries());
        }

        List<JsonAdaptedPerson> aggregatedPersons = new ArrayList<>();

        for (Map.Entry<PersonKey, List<JsonAdaptedAnniversary>> entry : groupedAnniversaries.entrySet()) {
            JsonAdaptedPerson mergedPerson = entry.getKey().toJsonAdaptedPerson(); // base person
            List<JsonAdaptedAnniversary> merged = entry.getValue().stream()
                    .distinct() // remove duplicates if equals() is overridden in JsonAdaptedAnniversary
                    .collect(Collectors.toList());
            mergedPerson.getAnniversaries().addAll(merged);
            aggregatedPersons.add(mergedPerson);
        }

        return new JsonSerializableAddressBook(aggregatedPersons);
    }
}
