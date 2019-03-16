package seedu.giatros.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.giatros.logic.CommandHistory;
import seedu.giatros.model.Model;

/**
 * Lists all persons in the giatros book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
