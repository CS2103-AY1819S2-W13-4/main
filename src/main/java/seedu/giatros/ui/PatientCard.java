package seedu.giatros.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.giatros.model.patient.Patient;

/**
 * An UI component that displays information of a {@code Patient}.
 */
public class PatientCard extends UiPart<Region> {

    private static final String FXML = "PatientListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on GiatrosBook level 4</a>
     */

    public final Patient patient;

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

    public PatientCard(Patient patient, int displayedIndex) {
        super(FXML);
        this.patient = patient;
        id.setText(displayedIndex + ". ");
        name.setText(patient.getName().fullName);
        phone.setText(patient.getPhone().value);
        address.setText(patient.getAddress().value);
        email.setText(patient.getEmail().value);
        createAllergy(patient);
    }

    /**
     * Chooses a random colour for {@code allergy}'s label.
     */
    private String chooseRandomColourFor(String allergy) {
        String[] listOfColours = { "blue", "red", "yellow", "green", "orange", "black", "white"};

        // Generate a random colour that will be consistent for the same allergy, based on the value of the string
        int valueOfString = 0;
        for (char character : allergy.toCharArray()) {
            valueOfString += (int) character;
        }

        return listOfColours[valueOfString % 7];
    }

    /**
     * Creates a coloured allergy label for {@code Patient}.
     */
    private void createAllergy(Patient patient) {
        patient.getAllergies().forEach(allergy -> {
            Label newAllergy = new Label(allergy.allergyName);
            newAllergy.getStyleClass().add(chooseRandomColourFor(allergy.allergyName));
            tags.getChildren().add(newAllergy);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PatientCard)) {
            return false;
        }

        // state check
        PatientCard card = (PatientCard) other;
        return id.getText().equals(card.id.getText())
                && patient.equals(card.patient);
    }
}
