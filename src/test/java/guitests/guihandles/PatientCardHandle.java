package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.giatros.model.patient.Patient;

/**
 * Provides a handle to a patient card in the patient list panel.
 */
public class PatientCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String PHONE_FIELD_ID = "#phone";
    private static final String EMAIL_FIELD_ID = "#email";
    private static final String ALLERGIES_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label addressLabel;
    private final Label phoneLabel;
    private final Label emailLabel;
    private final List<Label> allergyLabels;

    public PatientCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        nameLabel = getChildNode(NAME_FIELD_ID);
        addressLabel = getChildNode(ADDRESS_FIELD_ID);
        phoneLabel = getChildNode(PHONE_FIELD_ID);
        emailLabel = getChildNode(EMAIL_FIELD_ID);

        Region allergiesContainer = getChildNode(ALLERGIES_FIELD_ID);
        allergyLabels = allergiesContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getPhone() {
        return phoneLabel.getText();
    }

    public String getEmail() {
        return emailLabel.getText();
    }

    public List<String> getAllergies() {
        return allergyLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code patient}.
     */
    public boolean equals(Patient patient) {
        return getName().equals(patient.getName().fullName)
                && getAddress().equals(patient.getAddress().value)
                && getPhone().equals(patient.getPhone().value)
                && getEmail().equals(patient.getEmail().value)
                && ImmutableMultiset.copyOf(getAllergies()).equals(ImmutableMultiset.copyOf(patient.getAllergies()
                        .stream().map(allergy -> allergy.allergyName).collect(Collectors.toList())));
    }
}
