package seedu.giatros.logic.commands.account;

import static java.util.Objects.requireNonNull;

import seedu.giatros.commons.core.EventsCenter;
import seedu.giatros.commons.core.session.UserSession;
import seedu.giatros.commons.events.ui.ToggleSidePanelVisibilityEvent;
import seedu.giatros.commons.events.ui.accounts.LogoutEvent;
import seedu.giatros.logic.CommandHistory;
import seedu.giatros.logic.commands.Command;
import seedu.giatros.logic.commands.CommandResult;
import seedu.giatros.logic.commands.exceptions.CommandException;
import seedu.giatros.model.Model;

/**
 * Logs the user out, and destroy the {@code UserSession}.
 */
public class LogoutCommand extends Command {

    public static final String COMMAND_WORD = "logout";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Logout of the system. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "You have been logged out";
    public static final String MESSAGE_NOT_AUTHENTICATED = "You are not logged in";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!UserSession.isAuthenticated()) {
            throw new CommandException(MESSAGE_NOT_AUTHENTICATED);
        }

        model.resetGiatrosBookVersion();
        EventsCenter.getInstance().post(new LogoutEvent());
        EventsCenter.getInstance().post(new ToggleSidePanelVisibilityEvent(false));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
