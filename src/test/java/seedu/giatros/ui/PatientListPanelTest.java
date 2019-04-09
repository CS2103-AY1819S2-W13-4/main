package seedu.giatros.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.giatros.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.giatros.testutil.TypicalPatients.getTypicalPatients;
import static seedu.giatros.ui.testutil.GuiTestAssert.assertCardDisplaysPatient;
import static seedu.giatros.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.PatientCardHandle;
import guitests.guihandles.PatientListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.giatros.model.patient.Address;
import seedu.giatros.model.patient.Email;
import seedu.giatros.model.patient.Name;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.model.patient.Phone;

public class PatientListPanelTest extends GuiUnitTest {
    private static final ObservableList<Patient> TYPICAL_PATIENTS =
            FXCollections.observableList(getTypicalPatients());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Patient> selectedPatient = new SimpleObjectProperty<>();
    private PatientListPanelHandle patientListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_PATIENTS);

        for (int i = 0; i < TYPICAL_PATIENTS.size(); i++) {
            patientListPanelHandle.navigateToCard(TYPICAL_PATIENTS.get(i));
            Patient expectedPatient = TYPICAL_PATIENTS.get(i);
            PatientCardHandle actualCard = patientListPanelHandle.getPatientCardHandle(i);

            assertCardDisplaysPatient(expectedPatient, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedPatientChanged_selectionChanges() {
        initUi(TYPICAL_PATIENTS);
        Patient secondPatient = TYPICAL_PATIENTS.get(INDEX_SECOND_PATIENT.getZeroBased());
        guiRobot.interact(() -> selectedPatient.set(secondPatient));
        guiRobot.pauseForHuman();

        PatientCardHandle expectedPatient = patientListPanelHandle.getPatientCardHandle(INDEX_SECOND_PATIENT
                .getZeroBased());
        PatientCardHandle selectedPatient = patientListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPatient, selectedPatient);
    }

    /**
     * Verifies that creating and deleting large number of patients in {@code PatientListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Patient> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of patient cards exceeded time limit");
    }

    /**
     * Returns a list of patients containing {@code patientCount} patients that is used to populate the
     * {@code PatientListPanel}.
     */
    private ObservableList<Patient> createBackingList(int patientCount) {
        ObservableList<Patient> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < patientCount; i++) {
            Name name = new Name(i + "a");
            Phone phone = new Phone("000");
            Email email = new Email("a@aa");
            Address address = new Address("a");
            Patient patient = new Patient(name, phone, email, address, Collections.emptySet());
            backingList.add(patient);
        }
        return backingList;
    }

    /**
     * Initializes {@code patientListPanelHandle} with a {@code PatientListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code PatientListPanel}.
     */
    private void initUi(ObservableList<Patient> backingList) {
        PatientListPanel patientListPanel =
                new PatientListPanel(backingList, selectedPatient, selectedPatient::set);
        uiPartRule.setUiPart(patientListPanel);

        patientListPanelHandle = new PatientListPanelHandle(getChildNode(patientListPanel.getRoot(),
                patientListPanelHandle.PATIENT_LIST_VIEW_ID));
    }
}
