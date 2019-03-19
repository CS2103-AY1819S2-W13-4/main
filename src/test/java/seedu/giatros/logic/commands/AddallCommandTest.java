package seedu.giatros.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.giatros.logic.commands.AddallCommand.MESSAGE_ARGUMENTS;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ALLERGY_AMY;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ALLERGY_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.giatros.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.giatros.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.giatros.testutil.TypicalPatients.getTypicalGiatrosBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.giatros.logic.CommandHistory;
import seedu.giatros.model.Model;
import seedu.giatros.model.ModelManager;
import seedu.giatros.model.UserPrefs;
import seedu.giatros.model.patient.Allergy;

public class AddallCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute() {
        final Allergy allergy = new Allergy("One allergy, and some other allergy");
        Model model = new ModelManager(getTypicalGiatrosBook(), new UserPrefs());

        assertCommandFailure(new AddallCommand(INDEX_FIRST_PATIENT, allergy), model, commandHistory,
                String.format(MESSAGE_ARGUMENTS, INDEX_FIRST_PATIENT.getOneBased(), allergy));
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