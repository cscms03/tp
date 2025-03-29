package seedu.address.ui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A lightweight UI component that displays a birthday or work anniversary reminder for a {@code Person}.
 */
public class ReminderCard extends UiPart<Region> {
    private static final String FXML = "ReminderListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label jobPosition;
    @FXML
    private Label dateInfo;

    /**
     * Creates a {@code ReminderCard} with the given {@code Person}.
     * Highlights either the birthday or work anniversary depending on type.
     */
    public ReminderCard(Person person, boolean isBirthday) {
        super(FXML);
        this.person = person;
        name.setText(person.getName().fullName);
        jobPosition.setText(person.getJobPosition().value);

        LocalDate today = LocalDate.now();
        LocalDate nextDate = isBirthday
                ? person.getNextUpcomingBirthdayDate()
                : person.getNextUpcomingWorkAnniversaryDate();

        if (nextDate != null) {
            long daysLeft = ChronoUnit.DAYS.between(today, nextDate);
            String label = isBirthday ? "🎂 Birthday" : "🎉 Work Anniversary";
            dateInfo.setText(label + ": " + nextDate + " (in " + daysLeft + " day" + (daysLeft == 1 ? "" : "s") + ")");
        } else {
            assert false : "nextDate should not be null when person is in reminder list";
            dateInfo.setText("No upcoming " + (isBirthday ? "birthday." : "work anniversary."));
        }
    }
}
