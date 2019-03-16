package seedu.giatros.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.giatros.logic.CommandHistory;
import seedu.giatros.model.AddressBook;
import seedu.giatros.model.Model;

/**
 * Clears the giatros book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        model.commitAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
