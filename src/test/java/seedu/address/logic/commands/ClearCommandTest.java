package seedu.giatros.logic.commands;

import static seedu.giatros.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.giatros.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.giatros.logic.CommandHistory;
import seedu.giatros.model.AddressBook;
import seedu.giatros.model.Model;
import seedu.giatros.model.ModelManager;
import seedu.giatros.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitAddressBook();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());
        expectedModel.commitAddressBook();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
