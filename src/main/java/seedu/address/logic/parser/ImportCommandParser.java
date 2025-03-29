package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WRITE_MODE;
import static seedu.address.logic.parser.FilePathResolverUtils.verifyFileTypePresentAndValid;

import java.nio.file.Path;

import seedu.address.logic.commands.importexport.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {
    private static final String BLANK = "";
    private static final String WRITE_MODE_MESSAGE = "Write mode must be specified as either 'append' or 'overwrite'";
    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns a ImportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_FILEPATH,
                PREFIX_FILETYPE,
                PREFIX_FILENAME,
                PREFIX_WRITE_MODE
        );
        verifyFileTypePresentAndValid(argMultimap, ImportCommand.MESSAGE_USAGE);
        String filePath = argMultimap.getValue(PREFIX_FILEPATH).orElse(null);
        String filename = argMultimap.getValue(PREFIX_FILENAME).orElse(null);
        String fileType = argMultimap.getValue(PREFIX_FILETYPE).get();
        String writeMode = argMultimap.getValue(PREFIX_WRITE_MODE).orElseThrow(() ->
                new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, WRITE_MODE_MESSAGE)));
        if (!isValidWriteMode(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, WRITE_MODE_MESSAGE));
        }
        Path path = FilePathResolverUtils.resolveFilePath(filePath, filename, fileType);
        return new ImportCommand(fileType, path, writeMode);
    }

    private static boolean isValidWriteMode(ArgumentMultimap argMultimap) {
        return argMultimap.getValue(PREFIX_WRITE_MODE).get().equals("overwrite")
                || argMultimap.getValue(PREFIX_WRITE_MODE).get().equals("append");
    }
}
