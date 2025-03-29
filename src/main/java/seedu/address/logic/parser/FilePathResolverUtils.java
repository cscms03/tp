package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Utility class to resolve file paths.
 */
public class FilePathResolverUtils {
    private static final String BLANK = "";

    /**
     * Resolves the final file path based on an optional file path, filename, and fileType.
     * If both a file path (with a filename) and a filename are provided, throws an error.
     * Throws an error if the file extension does not match the provided file type.
     */
    public static Path resolveFilePath(String filePath, String filename, String fileType) throws ParseException {
        String expectedExtension = "." + fileType.toLowerCase();

        if (filePath != null) {
            Path filePathObj = Paths.get(filePath);
            boolean filePathHasFilename = looksLikeFile(filePathObj);

            // Prevent ambiguity when both a full file path and a separate filename are provided.
            if (filePathHasFilename && filename != null) {
                throw new ParseException("Provide either a full file path or a filename, not both.");
            }

            // If the file path already contains a filename, validate its extension.
            if (filePathHasFilename) {
                String actualName = filePathObj.getFileName().toString();
                validateFileExtension(actualName, expectedExtension, fileType);
                return filePathObj;
            }

            // If filePath is a directory, then a filename must be provided.
            if (filename == null) {
                throw new ParseException("Filename must be provided if path is just a directory.");
            }
            filename = ensureCorrectExtension(filename, expectedExtension);
            return filePathObj.resolve(filename);
        } else if (filename != null) {
            // When only a filename is provided, append the required extension if necessary.
            filename = ensureCorrectExtension(filename, expectedExtension);
            return Paths.get(filename);
        }
        throw new IllegalArgumentException("Either file path or filename must be provided.");
    }

    /**
     * Checks if the given path appears to represent a file.
     * This is based on a simple heuristic that the name contains a dot and does not end with one.
     */
    private static boolean looksLikeFile(Path path) {
        String name = path.getFileName().toString();
        return name.contains(".") && !name.endsWith(".");
    }

    /**
     * Ensures that the filename ends with the required extension.
     */
    private static String ensureCorrectExtension(String name, String requiredExt) {
        if (!name.toLowerCase().endsWith(requiredExt)) {
            return name + requiredExt;
        }
        return name;
    }

    /**
     * Validates that the file name's extension matches the expected extension.
     *
     * @param fileName The actual file name.
     * @param expectedExtension The expected extension (e.g., ".json", ".csv").
     * @param fileType The provided file type.
     * @throws ParseException if the extension does not match.
     */
    private static void validateFileExtension(String fileName, String expectedExtension, String fileType)
            throws ParseException {
        if (!fileName.toLowerCase().endsWith(expectedExtension)) {
            String actualExtension = fileName.substring(fileName.lastIndexOf('.'));
            throw new ParseException("The file extension " + actualExtension
                    + " does not match the provided file type " + fileType + ". Wrong parser used?");
        }
    }

    /**
     * Verifies that the file type is present and valid.
     *
     * @param argMultimap Contains user arguments.
     * @param commandType Message to print in case of a ParseException.
     * @throws ParseException If the file type is missing or invalid.
     */
    public static void verifyFileTypePresentAndValid(ArgumentMultimap argMultimap,
                                                     String commandType) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_FILETYPE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandType));
        }
        String fileTypeValue = argMultimap.getValue(PREFIX_FILETYPE).orElse(BLANK);
        if (!fileTypeValue.equals("json") && !fileTypeValue.equals("csv")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandType));
        }
    }
}
