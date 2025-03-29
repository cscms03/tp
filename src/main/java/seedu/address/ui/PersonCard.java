package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String EMPLOYEEID_PREFIX = "employeeId : ";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;
    private final MainWindow mainWindow; // Store reference to MainWindow

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label job;
    @FXML
    private Label email;
    @FXML
    private Label employeeId;
    @FXML
    private FlowPane tags;
    @FXML
    private Button showAnniversariesButton; // New button

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, MainWindow mainWindow) {
        super(FXML);
        this.person = person;
        this.mainWindow = mainWindow;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        job.setText(person.getJobPosition().value);
        email.setText(person.getEmail().value);
        employeeId.setText(person.getEmployeeId().toString());
        employeeId.setTooltip(new Tooltip(EMPLOYEEID_PREFIX + person.getEmployeeId().toString()));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @FXML
    void handleShowAnniversariesClick() {
        mainWindow.handleShowAnniversaries(person.getEmployeeId().toString());
    }

}
