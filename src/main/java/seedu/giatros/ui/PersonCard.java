package seedu.giatros.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.giatros.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        createTag(person);

    }
    /**
     * Chooses a random colour for {@code tag}'s label
     */
    private String chooseRandomColourFor(String tag) {
        String[] listOfColours = { "blue", "red", "yellow", "green", "orange", "black", "white"};

        // Generate a random colour that will be consistent for the same tag, based on the value of the string
        int valueOfString = 0;
        for (char character : tag.toCharArray()) {
            valueOfString += (int) character;
        }

        return listOfColours[valueOfString % 7];
    }

    /**
     * Creates a coloured tag label for {@code Person}
     */
    private void createTag(Person person) {
        person.getTags().forEach(tag -> {
            Label newTag = new Label(tag.tagName);
            newTag.getStyleClass().add(chooseRandomColourFor(tag.tagName));
            tags.getChildren().add(newTag);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
