package seedu.address.logic.commands.importexport;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WRITE_MODE;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import lombok.Getter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAdaptedPerson;
import seedu.address.storage.JsonSerializableAddressBook;
import seedu.address.storage.PersonKey;

/**
 * Imports data from a file.
 */
@Getter
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_USAGE = "import "
            + PREFIX_FILETYPE + "<json/csv> "
            + PREFIX_FILEPATH + "<fileName> "
            + PREFIX_FILEPATH + "<path> "
            + PREFIX_WRITE_MODE + "<append/overwrite>";
    public static final String MESSAGE_SUCCESS_OVERWRITE = "Successfully imported %d contacts,"
            + " overwriting existing data.";
    public static final String MESSAGE_SUCCESS_APPEND = """
            Successfully imported %d contacts, skipped %d.\s
            Please resolve conflicts manually\s
             + %s""";
    public static final String MESSAGE_INVALID_FILETYPE = "Invalid filetype. Use 'json' or 'csv'.";
    public static final String MESSAGE_INVALID_MODE = "Invalid mode. Use 'append' or 'overwrite'.";
    public static final String MESSAGE_INVALID_DATA = "Invalid data in import file: %s";
    private static final Logger logger = LogsCenter.getLogger(ImportCommand.class);
    public final String filetype;
    public final Path path;
    public final String mode;

    /**
     * Creates an ImportCommand to import the specified {@code Person}
     * @param filetype json or csv source file
     * @param path path to the file
     * @param mode append or replace
     */
    public ImportCommand(String filetype, Path path, String mode) {
        this.filetype = filetype;
        this.path = path;
        this.mode = mode;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            // Import data from file
            JsonSerializableAddressBook importedData;
            if (filetype.equalsIgnoreCase("json")) {
                importedData = AddressBookFormatConverter.importFromJson(path);
                logger.info(String.format("Importing from JSON + %s, %s", path , importedData));
            } else if (filetype.equalsIgnoreCase("csv")) {
                importedData = AddressBookFormatConverter.importFromCsv(path);
                logger.info(String.format("Importing from CSV + %s, %s", path , importedData));
            } else {
                throw new CommandException(MESSAGE_INVALID_FILETYPE);
            }

            // Handle different import modes
            if (mode.equalsIgnoreCase("overwrite")) {
                return handleOverwriteMode(model, importedData);
            } else if (mode.equalsIgnoreCase("append")) {
                return handleAppendMode(model, importedData);
            } else {
                throw new CommandException(MESSAGE_INVALID_MODE);
            }
        } catch (IOException e) {
            throw new CommandException("Error reading file: " + e.getMessage());
        } catch (DataLoadingException e) {
            throw new CommandException("Error loading data: " + e.getMessage());
        }
    }


    /**
     * Handles the overwrite mode by replacing the entire address book.
     * Before overwriting, we aggregate the imported data and remove entries that have the same employeeId
     * but conflicting PersonKey details.
     */
    private CommandResult handleOverwriteMode(Model model, JsonSerializableAddressBook importedData)
            throws CommandException {
        try {
            AggregationResult aggResult = aggregateImportedData(importedData);
            if (!aggResult.conflicts.isEmpty()) {
                String errorMessage = buildConflictErrorMessage(aggResult.conflicts);
                throw new CommandException(errorMessage);
            }
            // Build a new AddressBook from the aggregated persons.
            AddressBook newAddressBook = new AddressBook();
            for (Person p : aggResult.aggregated) {
                newAddressBook.addPerson(p);
            }
            model.setAddressBook(newAddressBook);
            int importedCount = newAddressBook.getPersonList().size();
            return new CommandResult(String.format(MESSAGE_SUCCESS_OVERWRITE, importedCount));
        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_DATA, e.getMessage()));
        }
    }

    /**
     * Handles the append mode by adding persons individually.
     * The method first aggregates the imported data by removing internal duplicates (conflicts)
     * and then compares each aggregated entry with the model. For entries that have the same employeeId
     * as an existing record but with different details (as determined by hasSameDetails), the import is skipped.
     * Both conflict lists (internal conflicts and model conflicts) are returned.
     */
    private CommandResult handleAppendMode(Model model, JsonSerializableAddressBook importedData)
            throws CommandException {
        try {
            List<List<Person>> importStats = processImportedPersonsWhenAppend(model, importedData);
            int importedCount = importStats.get(0).size();
            List<Person> skippedList = importStats.get(1);
            int skippedCount = skippedList.size();
            String skippedDetails = buildConflictErrorMessage(skippedList);
            return new CommandResult(String.format(MESSAGE_SUCCESS_APPEND,
                    importedCount, skippedCount, skippedDetails));
        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_DATA, e.getMessage()));
        }
    }

    /**
     * Processes the imported data in append mode.
     * First, it aggregates the imported persons by removing internal duplicates
     * (i.e. those with the same employeeId but differing PersonKey details).
     * Then, for each aggregated person, it checks for a conflict with the model:
     * if the model already contains a person with the same employeeId but different details,
     * the imported person is flagged as a conflict.
     * Returns a list of two lists:
     * - index 0: persons successfully imported (added or merged)
     * - index 1: persons that were skipped due to conflicts.
     */
    private List<List<Person>> processImportedPersonsWhenAppend(Model model,
                                                                JsonSerializableAddressBook importedData)
            throws IllegalValueException, CommandException {
        // First, remove internal duplicates/conflicts from the imported data.
        AggregationResult aggResult = aggregateImportedData(importedData);
        List<Person> aggregatedImported = aggResult.aggregated;
        // Start with the internal conflicts as already omitted.
        List<Person> omittedPersons = new ArrayList<>(aggResult.conflicts);
        List<Person> importedPersons = new ArrayList<>();

        // Now check each aggregated person against the model.
        for (Person personToImport : aggregatedImported) {
            Person matchInModel = model.getFilteredByEmployeeIdPrefixList(
                            EmployeeId.fromString(personToImport.getEmployeeId().toString()))
                    .stream()
                    .filter(p -> p.isSamePerson(personToImport))
                    .findFirst()
                    .orElse(null);
            if (matchInModel == null) {
                // No matching person in model – add new record.
                model.addPerson(personToImport);
                importedPersons.add(personToImport);
            } else if (matchInModel.hasSameDetails(personToImport)) {
                // Matching person exists with same details – merge anniversary lists.
                mergeAnniversaries(matchInModel, personToImport);
                importedPersons.add(personToImport);
            } else {
                // Conflict with an existing model record.
                omittedPersons.add(personToImport);
            }
        }
        return List.of(importedPersons, omittedPersons);
    }

    /**
     * Aggregates the imported data by grouping by employeeId.
     * For a given employeeId, if all records have the same PersonKey (i.e. same details), they are merged
     * (their anniversaries are combined). Otherwise, if conflicting details are found for the same employeeId,
     * none are aggregated and all are flagged as internal conflicts.
     *
     * @return an AggregationResult containing:
     *         - aggregated: a list of valid Person objects ready for import.
     *         - conflicts: a list of Person objects that were removed due to conflicting details.
     */
    private AggregationResult aggregateImportedData(JsonSerializableAddressBook importedData)
            throws IllegalValueException {
        Map<String, Person> aggregated = new HashMap<>();
        // For employeeIds that have conflicts, we use a set to record all differing PersonKeys.
        Set<String> conflictEmployeeIds = new HashSet<>();

        for (JsonAdaptedPerson adapted : importedData.getPersons()) {
            Person person = adapted.toModelType();
            String employeeId = person.getEmployeeId().toString();
            PersonKey key = PersonKey.from(adapted);
            if (aggregated.containsKey(employeeId)) {
                Person existing = aggregated.get(employeeId);
                PersonKey existingKey = PersonKey.from(existing);
                if (!existingKey.equals(key)) {
                    // Conflict: remove any previously aggregated person with this employeeId.
                    conflictEmployeeIds.add(employeeId);
                } else {
                    // Same details: merge anniversary lists.
                    mergeAnniversaries(existing, person);
                }
            } else if (conflictEmployeeIds.contains(employeeId)) {
                // Already flagged as conflict; do nothing.
                continue;
            } else {
                aggregated.put(employeeId, person);
            }
        }

        // Build conflict list: all adapted persons whose employeeId is flagged as conflicting.
        List<Person> conflicts = importedData.getPersons().stream()
                .map(adapted -> {
                    try {
                        return adapted.toModelType();
                    } catch (IllegalValueException e) {
                        // In case of conversion error, wrap it in a runtime exception.
                        throw new RuntimeException(e);
                    }
                })
                .filter(person -> conflictEmployeeIds.contains(person.getEmployeeId().toString()))
                .collect(Collectors.toList());

        return new AggregationResult(new ArrayList<>(aggregated.values()), conflicts);
    }

    /**
     * Merges the anniversaries from the source person into the target person.
     */
    private void mergeAnniversaries(Person target, Person source) {
        target.getAnniversaries().addAll(source.getAnniversaries());
    }

    /**
     * Builds an error message listing the full details of persons that have conflicting records.
     */
    private String buildConflictErrorMessage(List<Person> conflictPersons) {
        return conflictPersons.stream()
                .map(this::formatPersonDetails)
                .collect(Collectors.joining("\n", "Conflicting records found:\n", ""));
    }
    /**
     * Formats the details of a Person into a readable string.
     */
    private String formatPersonDetails(Person person) {
        return String.format("Name: %s, Phone: %s, Email: %s, Job Position: %s, EmployeeID: %s, Tags: %s",
                person.getName(), person.getPhone(), person.getEmail(), person.getJobPosition(),
                person.getEmployeeId().toString(), person.getTags().toString());
    }
    /**
     * A helper class to hold the result of aggregating imported persons.
     * Contains a list of aggregated persons (with internal duplicates merged) and
     * a list of persons that were removed due to internal conflicts.
     */
    private static class AggregationResult {
        final List<Person> aggregated;
        final List<Person> conflicts;

        AggregationResult(List<Person> aggregated, List<Person> conflicts) {
            this.aggregated = aggregated;
            this.conflicts = conflicts;
        }
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ImportCommand otherCommand)) {
            return false;
        }
        return filetype.equals(otherCommand.filetype)
                && path.equals(otherCommand.path)
                && mode.equals(otherCommand.mode);
    }
}
