package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.FilePathResolverUtils.verifyFileTypePresentAndValid;

import java.nio.file.Path;

import seedu.address.logic.commands.importexport.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns a ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_FILEPATH,
                PREFIX_FILETYPE,
                PREFIX_FILENAME
        );

        String trimmedArgs = args.trim();
        verifyFileTypePresentAndValid(argMultimap, ExportCommand.MESSAGE_USAGE);
        String filePath = argMultimap.getValue(PREFIX_FILEPATH).orElse(null);
        String filename = argMultimap.getValue(PREFIX_FILENAME).orElse(null);
        String fileType = argMultimap.getValue(PREFIX_FILETYPE).get();
        Path path = null;
        if (filePath != null || filename != null) {
            path = FilePathResolverUtils.resolveFilePath(filePath, filename, fileType);
        }
        return new ExportCommand(fileType, path);
    }
}
