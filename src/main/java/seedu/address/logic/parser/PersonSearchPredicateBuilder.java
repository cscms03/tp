package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.JobPositionContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Builds a predicate based on the given {@code ArgumentMultimap}.
 */
public class PersonSearchPredicateBuilder {

    private static final Logger logger = LogsCenter.getLogger(PersonSearchPredicateBuilder.class);
    /**
     * Builds a {@code Predicate<Person>} based on the given {@code ArgumentMultimap}.
     * @param argMultimap the argument multimap
     * @return the predicate
     */
    public static Predicate<Person> buildPredicate(ArgumentMultimap argMultimap) {
        Predicate<Person> combinedPredicate = person -> true;

        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasJob = argMultimap.getValue(PREFIX_JOBPOSITION).isPresent();

        if (hasName) {
            String name = argMultimap.getValue(PREFIX_NAME).get().trim();
            String[] nameKeywords = name.split("\\s+");
            Predicate<Person> namePredicate = new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords));
            logger.info("Predicate added: " + namePredicate);
            combinedPredicate = combinedPredicate.and(namePredicate);
        }

        if (hasJob) {
            String job = argMultimap.getValue(PREFIX_JOBPOSITION).get().trim();
            String[] jobKeywords = job.split("\\s+");
            Predicate<Person> jpPredicate = new JobPositionContainsKeywordsPredicate(Arrays.asList(jobKeywords));
            logger.info("Predicate added: " + jpPredicate);
            combinedPredicate = combinedPredicate.and(jpPredicate);
        }

        return combinedPredicate;
    }
}
