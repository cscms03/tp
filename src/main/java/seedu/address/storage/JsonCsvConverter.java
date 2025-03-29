package seedu.address.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Converts between JSON and CSV formats with proper handling of complex structures.
 */
public class JsonCsvConverter {
    public static final String EMPLOYEEID = "employeeId";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String JOBPOSITION = "jobposition";
    public static final String TAGS = "tags";
    public static final String ANNIVERSARYDATE = "anniversaryDate";
    public static final String ANNIVERSARYTYPE = "anniversaryType";
    public static final String ANNIVERSARYTYPEDESC = "anniversaryTypeDesc";
    public static final String ANNIVERSARYNAME = "anniversaryName";
    public static final String ANNIVERSARYDESCRIPTION = "anniversaryDescription";
    public static final String ANNIVERSARIES = "anniversaries";
    public static final String PERSONS = "persons";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    private final ObjectMapper mapper;
    private final QueryValidator validator;

    /**
     * Creates a new JsonCsvConverter with the given ObjectMapper and QueryValidator.
     * @param mapper The ObjectMapper to use for JSON conversion.
     * @param validator The QueryValidator to use for validation.
     */
    public JsonCsvConverter(ObjectMapper mapper, QueryValidator validator) {
        this.mapper = mapper;
        this.validator = validator;
    }

    /**
     * Converts an object to CSV format.
     * @param obj The object to convert.
     * @return The CSV representation of the object.
     * @throws JsonProcessingException If the object cannot be converted.
     */
    public String toCsv(Object obj) throws JsonProcessingException {
        validator.validateForWrite(obj);
        JsonNode rootNode = mapper.valueToTree(obj);
        StringBuilder csvBuilder = new StringBuilder();
        if (rootNode.has("persons") && rootNode.get("persons").isArray()) {
            ArrayNode persons = (ArrayNode) rootNode.get("persons");
            // Updated CSV header includes anniversaryTypeDesc
            List<String> headers = Arrays.asList(
                    "employeeId", "name", "phone", "email", "jobposition", "tags",
                    "anniversaryDate", "anniversaryType", "anniversaryTypeDesc",
                    "anniversaryName", "anniversaryDescription"
            );
            csvBuilder.append(String.join(",", headers)).append("\n");
            for (JsonNode person : persons) {
                String employeeId = escapeForCsv(person.path("employeeId").asText());
                String name = escapeForCsv(person.path("name").asText());
                String phone = escapeForCsv(person.path("phone").asText());
                String email = escapeForCsv(person.path("email").asText());
                String jobPosition = escapeForCsv(person.path("jobposition").asText());
                // Process tags array
                JsonNode tags = person.path("tags");
                StringBuilder tagsStr = new StringBuilder();
                if (tags.isArray()) {
                    for (int i = 0; i < tags.size(); i++) {
                        if (i > 0) {
                            tagsStr.append(";");
                        }
                        tagsStr.append(tags.get(i).asText());
                    }
                }
                String tagsFormatted = escapeForCsv(tagsStr.toString());
                // Process anniversaries array
                JsonNode anniversaries = person.path("anniversaries");
                if (anniversaries.isArray() && anniversaries.size() > 0) {
                    for (JsonNode anniv : anniversaries) {
                        String annivDate = escapeForCsv(anniv.path("date").asText());
                        // Expand the type node into two separate CSV columns:
                        String annivTypeName = escapeForCsv(anniv.path("type").path("name").asText());
                        String annivTypeDesc = escapeForCsv(anniv.path("type").path("description").asText());
                        String annivName = escapeForCsv(anniv.path("name").asText());
                        String annivDescription = escapeForCsv(anniv.path("description").asText());

                        csvBuilder.append(String.join(",",
                                        employeeId, name, phone, email, jobPosition, tagsFormatted,
                                        annivDate, annivTypeName, annivTypeDesc, annivName, annivDescription))
                                .append("\n");
                    }
                } else {
                    // No anniversaries: Insert a single row with empty anniversary fields
                    csvBuilder.append(String.join(",",
                                    employeeId, name, phone, email, jobPosition, tagsFormatted,
                                    "", "", "", "", ""))
                            .append("\n");
                }
            }
        }
        return csvBuilder.toString();
    }


    /**
     * Escapes values for CSV output.
     * Ensures proper formatting by handling commas, quotes, and newlines.
     */
    private String escapeForCsv(String value) {
        if (value == null || value.isEmpty()) {
            return "\"\"";
        }

        // Escape quotes by doubling them and wrap in quotes if necessary
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Converts CSV data to an object.
     * @param csvData The CSV data to convert.
     * @param clazz The class of the object to convert to.
     * @param <T> The type of the object to convert to.
     * @return The object represented by the CSV data.
     * @throws IOException If the CSV data cannot be converted.
     */
    public <T> T fromCsv(String csvData, Class<T> clazz) throws IOException, CommandException {
        validator.validateForRead(csvData);

        // For JsonSerializableAddressBook, use special parser
        if (clazz.equals(JsonSerializableAddressBook.class)) {
            return parseAddressBookCsv(csvData, clazz);
        }

        throw new IOException("Unsupported conversion from CSV to " + clazz.getName());
    }

    /**
     * Parses CSV data into a JsonSerializableAddressBook object.
     * The resulting JsonSerializableAddressBook Object is unsafe. calling TomodelType can cause errors
     * this is because there may exist Persons with the same employeeId but different details
     * @param csvData The CSV data to parse.
     * @param clazz The class of the object to convert to.
     * @param <T> The type of the object to convert to.
     * @return The object represented by the CSV data.
     * @throws IOException If the CSV data cannot be converted.
     * @throws CommandException If the CSV data is invalid.
     */
    private <T> T parseAddressBookCsv(String csvData, Class<T> clazz) throws IOException, CommandException {
        // Normalize Windows line endings to Unix style
        csvData = csvData.replace("\r\n", "\n");
        String[] lines = csvData.split("\n");
        if (lines.length < 1) {
            throw new IOException("CSV data is empty");
        }

        // Parse header line
        String[] headers = parseCsvLine(lines[0]);

        // Detect a shifted header order.
        // Expected order: EMPLOYEEID, NAME, PHONE, EMAIL, JOBPOSITION, TAGS,
        // ANNIVERSARYDATE, ANNIVERSARYTYPE, ANNIVERSARYNAME, ANNIVERSARYDESCRIPTION
        // If the header starts with "jobposition" instead of "employeeId", assume it is shifted.
        if (headers.length == 10 && headers[0].equalsIgnoreCase(JOBPOSITION)) {
            String[] correctedHeaders = new String[headers.length];
            correctedHeaders[0] = headers[1]; // should be EMPLOYEEID
            correctedHeaders[1] = headers[2]; // should be NAME
            correctedHeaders[2] = headers[3]; // should be PHONE
            correctedHeaders[3] = headers[4]; // should be EMAIL
            correctedHeaders[4] = headers[0]; // jobposition moves to index 4
            for (int i = 5; i < headers.length; i++) {
                correctedHeaders[i] = headers[i];
            }
            headers = correctedHeaders;
        }

        // Build a lookup for header indices (this method also checks for required fields)
        Map<String, Integer> headerIndices = getAndCheckHeaderField(headers);

        // Instead of grouping rows by employeeId, create a persons array node with one entry per row.
        ArrayNode personsArray = mapper.createArrayNode();

        for (int i = 1; i < lines.length; i++) {
            String[] values = parseCsvLine(lines[i]);
            if (values.length < headers.length) {
                continue;
            }

            ObjectNode personNode = mapper.createObjectNode();
            personNode.put(EMPLOYEEID, values[headerIndices.get(EMPLOYEEID)].trim());
            personNode.put(NAME, values[headerIndices.get(NAME)].trim());
            personNode.put(PHONE, values[headerIndices.get(PHONE)].trim());
            personNode.put(EMAIL, values[headerIndices.get(EMAIL)].trim());
            personNode.put(JOBPOSITION, values[headerIndices.get(JOBPOSITION)].trim());

            // Process tags (split by semicolon) and trim each tag
            String tagsStr = values[headerIndices.get(TAGS)].trim();
            ArrayNode tagsArray = mapper.createArrayNode();
            if (!tagsStr.isEmpty()) {
                for (String tag : tagsStr.split(";")) {
                    tagsArray.add(tag.trim());
                }
            }
            personNode.set(TAGS, tagsArray);

            // Process anniversary columns from this row, trimming each value
            String annivDate = values[headerIndices.get(ANNIVERSARYDATE)].trim();
            String annivType = values[headerIndices.get(ANNIVERSARYTYPE)].trim();
            String annivTypeDesc = values[headerIndices.get(ANNIVERSARYTYPEDESC)].trim();
            String annivName = values[headerIndices.get(ANNIVERSARYNAME)].trim();
            String annivDescription = values[headerIndices.get(ANNIVERSARYDESCRIPTION)].trim();

            ArrayNode annivsArray = mapper.createArrayNode();
            // Only add an anniversary if at least one field is non-empty
            if (!annivDate.isEmpty() || !annivType.isEmpty() || !annivName.isEmpty() || !annivDescription.isEmpty()) {
                ObjectNode annivNode = mapper.createObjectNode();
                annivNode.put(DATE, annivDate);
                // Create proper structure for anniversary type (oversight)
                ObjectNode typeNode = mapper.createObjectNode();
                typeNode.put("name", annivType);
                typeNode.put("description", annivTypeDesc); // Default empty description
                annivNode.set(TYPE, typeNode);
                annivNode.put(NAME, annivName);
                annivNode.put(DESCRIPTION, annivDescription);
                annivsArray.add(annivNode);
            }
            personNode.set(ANNIVERSARIES, annivsArray);

            personsArray.add(personNode);
        }

        // Build the root node with the persons array
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set(PERSONS, personsArray);

        return mapper.convertValue(rootNode, clazz);
    }

    private static Map<String, Integer> getAndCheckHeaderField(String[] headers) throws CommandException {
        Map<String, Integer> headerIndices = new HashMap<>();
        for (int j = 0; j < headers.length; j++) {
            headerIndices.put(headers[j], j);
        }
        // Now require the new anniversaryTypeDesc field as well.
        String[] requiredFields = {
            EMPLOYEEID, NAME, PHONE, EMAIL, JOBPOSITION, TAGS,
            ANNIVERSARYDATE, ANNIVERSARYTYPE, ANNIVERSARYTYPEDESC, ANNIVERSARYNAME, ANNIVERSARYDESCRIPTION
        };
        for (String field : requiredFields) {
            if (!headerIndices.containsKey(field)) {
                throw new CommandException("CSV missing required field: " + field);
            }
        }
        return headerIndices;
    }


    String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++;
                } else {
                    // Toggle quote mode
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                result.add(unescapeFromCsv(currentField.toString()));
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        result.add(unescapeFromCsv(currentField.toString()));
        return result.toArray(new String[0]);
    }

    /**
     * Unescapes a field from CSV format.
     * @param field The field to unescape.
     * @return The unescaped field.
     */
    private String unescapeFromCsv(String field) {
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }
        return field.replace("\"\"", "\"");
    }
}
