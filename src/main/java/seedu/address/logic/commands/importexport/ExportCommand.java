package seedu.address.logic.commands.importexport;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;

import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import lombok.Getter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports currently visible persons data to a file.
 */
@Getter
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_USAGE = "export "
            + PREFIX_FILETYPE + "<json / csv> ["
            + PREFIX_FILEPATH + "<path>]";
    private static final Logger logger = LogsCenter.getLogger(ExportCommand.class);
    public final String filetype;
    public final Path path;

    /**
     * Creates an ExportCommand to export the specified {@code Person}
     * @param filetype json or csv source file
     * @param path path to the file
     */
    public ExportCommand(String filetype, Path path) {
        this.filetype = filetype;
        this.path = path;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        ObservableList<Person> displayedPeople = model.getFilteredPersonList();
        if (displayedPeople.isEmpty()) {
            throw new CommandException("No people to export.");
        }
        try {
            if (filetype.equals("json")) {
                logger.info(String.format("Exporting to json + %s, %s", path , displayedPeople));
                AddressBookFormatConverter.exportToJson(displayedPeople, path);
            } else if (filetype.equals("csv")) {
                logger.info(String.format("Exporting to CSV + %s, %s", path , displayedPeople));
                AddressBookFormatConverter.exportToCsv(displayedPeople, path);
            } else {
                throw new CommandException("Invalid filetype. " + MESSAGE_USAGE);
            }
        } catch (Exception e) {
            throw new CommandException("Error exporting data: " + e.getMessage());
        }
        return new CommandResult("Exported " + displayedPeople + " people in " + filetype
                + " format to " + path);
    }
}
