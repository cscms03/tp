package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class FindCommandParserTest {

    private Model model;
    private FindCommandParser parser = new FindCommandParser();

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(Model.class);
    }
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_filtersCorrectly() throws ParseException {
        // no leading and trailing whitespaces
        FindCommand command = parser.parse(" n/Alice Bob");

        Person matching1 = new PersonBuilder().withName("Alice Johnson").build();
        Person matching2 = new PersonBuilder().withName("Bob Marley").build();
        Person nonMatching = new PersonBuilder().withName("Charlie Brown").build();

        ObservableList<Person> baseList =
                FXCollections.observableArrayList(matching1, matching2, nonMatching);
        FilteredList<Person> filteredList = new FilteredList<>(baseList);

        //stub creation
        when(model.getFilteredPersonList()).thenReturn(filteredList);

        doAnswer(invocation -> {
            Predicate<Person> predicate = invocation.getArgument(0);
            filteredList.setPredicate(predicate);
            return null;
        }).when(model).updateFilteredPersonList(any());

        command.execute(model);

        assertEquals(List.of(matching1, matching2), filteredList);

        //reset list
        filteredList.setPredicate(unused -> true);

        // multiple whitespaces between keywords
        FindCommand whitespaceCommand = parser.parse(" \n n/Alice \n \t Bob  \t");

        whitespaceCommand.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredPersonList());
    }

    @Test
    public void parse_jobPositionArgs_filtersCorrectly() throws Exception {
        // Parse command with standard input
        FindCommand command = parser.parse(" jp/engineer manager");

        Person matching1 = new PersonBuilder().withJobPosition("Software Engineer").build();
        Person matching2 = new PersonBuilder().withJobPosition("Product Manager").build();
        Person nonMatching = new PersonBuilder().withJobPosition("Sales Associate").build();

        ObservableList<Person> baseList =
                FXCollections.observableArrayList(matching1, matching2, nonMatching);
        FilteredList<Person> filteredList = new FilteredList<>(baseList);

        when(model.getFilteredPersonList()).thenReturn(filteredList);

        doAnswer(invocation -> {
            Predicate<Person> predicate = invocation.getArgument(0);
            filteredList.setPredicate(predicate);
            return null;
        }).when(model).updateFilteredPersonList(any());

        command.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredPersonList());

        //reset list
        filteredList.setPredicate(unused -> true);

        // Parse command with messy whitespace
        FindCommand whitespaceCommand = parser.parse(" \n jp/engineer \t manager  \n");

        whitespaceCommand.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredPersonList());
    }

    @Test
    public void parse_nameAndJobPositionArgs_filtersCorrectly() throws Exception {
        // Parse command with both name and job position
        FindCommand command = parser.parse(" n/jack jp/engineer");

        Person matching = new PersonBuilder()
                .withName("Jack Daniel")
                .withJobPosition("Software Engineer")
                .build();

        Person wrongName = new PersonBuilder()
                .withName("Alice")
                .withJobPosition("Software Engineer")
                .build();

        Person wrongJob = new PersonBuilder()
                .withName("Jack")
                .withJobPosition("Chef")
                .build();

        Person completelyOff = new PersonBuilder()
                .withName("Bob")
                .withJobPosition("Accountant")
                .build();

        ObservableList<Person> baseList =
                FXCollections.observableArrayList(matching, wrongName, wrongJob, completelyOff);
        FilteredList<Person> filteredList = new FilteredList<>(baseList);

        when(model.getFilteredPersonList()).thenReturn(filteredList);

        doAnswer(invocation -> {
            Predicate<Person> predicate = invocation.getArgument(0);
            filteredList.setPredicate(predicate);
            return null;
        }).when(model).updateFilteredPersonList(any());

        command.execute(model);

        assertEquals(List.of(matching), model.getFilteredPersonList());

        //reset list
        filteredList.setPredicate(unused -> true);

        // Messy whitespace version
        FindCommand messyCommand = parser.parse(" \n n/jack  \n jp/engineer \t");

        messyCommand.execute(model);

        assertEquals(List.of(matching), model.getFilteredPersonList());
    }
}
