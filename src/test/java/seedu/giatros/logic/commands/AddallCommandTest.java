package seedu.giatros.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ALLERGY_AMY;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ALLERGY_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.giatros.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.giatros.logic.commands.CommandTestUtil.showPatientAtIndex;
import static seedu.giatros.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.giatros.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.giatros.testutil.TypicalPatients.getTypicalGiatrosBook;

import org.junit.Test;

import seedu.giatros.commons.core.Messages;
import seedu.giatros.commons.core.index.Index;
import seedu.giatros.logic.CommandHistory;
import seedu.giatros.model.GiatrosBook;
import seedu.giatros.model.Model;
import seedu.giatros.model.ModelManager;
import seedu.giatros.model.UserPrefs;
import seedu.giatros.model.allergy.Allergy;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.testutil.PatientBuilder;

public class AddallCommandTest {

    private static final String ALLERGY_STUB = "someAllergy";

    private Model model = new ModelManager(getTypicalGiatrosBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_addAllergyUnfilteredList_success() {
        Patient firstPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedPatient = new PatientBuilder(firstPatient).withAllergy(ALLERGY_STUB).build();

        AddallCommand addallCommand = new AddallCommand(INDEX_FIRST_PATIENT, editedPatient.getAllergies());

        String expectedMessage = String.format(AddallCommand.MESSAGE_ADD_ALLERGY_SUCCESS, editedPatient);

        Model expectedModel = new ModelManager(new GiatrosBook(model.getGiatrosBook()), new UserPrefs());
        expectedModel.setPatient(firstPatient, editedPatient);
        expectedModel.commitGiatrosBook();

        assertCommandSuccess(addallCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        Patient firstPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedPatient = new PatientBuilder(model.getFilteredPatientList()
                .get(INDEX_FIRST_PATIENT.getZeroBased())).withAllergy(ALLERGY_STUB).build();

        AddallCommand addallCommand = new AddallCommand(INDEX_FIRST_PATIENT, editedPatient.getAllergies());

        String expectedMessage = String.format(AddallCommand.MESSAGE_ADD_ALLERGY_SUCCESS, editedPatient);

        Model expectedModel = new ModelManager(new GiatrosBook(model.getGiatrosBook()), new UserPrefs());
        expectedModel.setPatient(firstPatient, editedPatient);
        expectedModel.commitGiatrosBook();

        assertCommandSuccess(addallCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        AddallCommand addallCommand = new AddallCommand(outOfBoundIndex, new Allergy(VALID_ALLERGY_BOB));

        assertCommandFailure(addallCommand, model, commandHistory, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of giatros book
     */
    @Test
    public void execute_invalidPatientIndexFilteredList_failure() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);
        Index outOfBoundIndex = INDEX_SECOND_PATIENT;
        // ensures that outOfBoundIndex is still in bounds of giatros book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getGiatrosBook().getPatientList().size());

        AddallCommand addallCommand = new AddallCommand(outOfBoundIndex, new Allergy(VALID_ALLERGY_BOB));

        assertCommandFailure(addallCommand, model, commandHistory, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Patient patientToModify = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient modifiedPatient = new PatientBuilder(patientToModify).withAllergy(ALLERGY_STUB).build();
        AddallCommand addallCommand = new AddallCommand(INDEX_FIRST_PATIENT, new Allergy(ALLERGY_STUB));
        Model expectedModel = new ModelManager(model.getGiatrosBook(), new UserPrefs());
        expectedModel.setPatient(patientToModify, modifiedPatient);
        expectedModel.commitGiatrosBook();

        // addall -> first patient allergy changed
        addallCommand.execute(model, commandHistory);

        // undo -> reverts giatrosbook back to previous state and filtered patient list to show all patients
        expectedModel.undoGiatrosBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first patient modified again
        expectedModel.redoGiatrosBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        AddallCommand addallCommand = new AddallCommand(outOfBoundIndex, new Allergy("none"));

        // execution failed -> giatros book state not added into model
        assertCommandFailure(addallCommand, model, commandHistory, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);

        // single giatros book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Modifies {@code Patient#allergy} from a filtered list.
     * 2. Undo the modification.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously modified patient in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the modification. This ensures {@code RedoCommand} modifies the patient object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePatientDeleted() throws Exception {
        AddallCommand addallCommand = new AddallCommand(INDEX_FIRST_PATIENT, new Allergy(ALLERGY_STUB));
        Model expectedModel = new ModelManager(model.getGiatrosBook(), new UserPrefs());

        showPatientAtIndex(model, INDEX_SECOND_PATIENT);
        Patient patientToModify = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient modifiedPatient = new PatientBuilder(patientToModify).withAllergy(ALLERGY_STUB).build();
        expectedModel.setPatient(patientToModify, modifiedPatient);
        expectedModel.commitGiatrosBook();

        // addall -> modifies second patient in unfiltered patient list / first patient in filtered patient list
        addallCommand.execute(model, commandHistory);

        // undo -> reverts giatrosbook back to previous state and filtered patient list to show all patients
        expectedModel.undoGiatrosBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> modifies same second patient in unfiltered patient list
        expectedModel.redoGiatrosBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final AddallCommand standardCommand = new AddallCommand(INDEX_FIRST_PATIENT, new Allergy(VALID_ALLERGY_AMY));

        // same values -> returns true
        AddallCommand commandWithSameValues = new AddallCommand(INDEX_FIRST_PATIENT, new Allergy(VALID_ALLERGY_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddallCommand(INDEX_SECOND_PATIENT, new Allergy(VALID_ALLERGY_AMY))));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new AddallCommand(INDEX_FIRST_PATIENT, new Allergy(VALID_ALLERGY_BOB))));
    }

}
