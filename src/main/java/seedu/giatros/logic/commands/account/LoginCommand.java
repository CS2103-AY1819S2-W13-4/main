package seedu.giatros.logic.commands.account;

import static java.util.Objects.requireNonNull;
import static seedu.giatros.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.giatros.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.giatros.commons.core.EventsCenter;
import seedu.giatros.commons.core.session.UserSession;
import seedu.giatros.commons.events.ui.ToggleSidePanelVisibilityEvent;
import seedu.giatros.commons.events.ui.accounts.LoginEvent;
import seedu.giatros.logic.CommandHistory;
import seedu.giatros.logic.commands.Command;
import seedu.giatros.logic.commands.CommandResult;
import seedu.giatros.logic.commands.exceptions.CommandException;
import seedu.giatros.model.Model;
import seedu.giatros.model.account.Account;

/**
 * Logs the user into an existing {@code Account}, and create a {@code UserSession}.
 */
public class LoginCommand extends Command {

    public static final String COMMAND_WORD = "login";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Login to the system with an existing account. "
            + "Parameters: "
            + PREFIX_ID + "USERNAME "
            + PREFIX_PASSWORD + "PASSWORD\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "chuaes "
            + PREFIX_PASSWORD + "112233";

    public static final String MESSAGE_SUCCESS = "Successfully logged in to %s";
    public static final String MESSAGE_ACCOUNT_NOT_FOUND = "The account does not exist";
    public static final String MESSAGE_WRONG_PASSWORD = "The credential is invalid";
    public static final String MESSAGE_ALREADY_AUTHENTICATED = "You are already logged in";

    private final Account toLogin;

    public LoginCommand(Account toLogin) {
        requireNonNull(toLogin);
        this.toLogin = toLogin;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        Account retrievedAccount;

        if (UserSession.isAuthenticated()) {
            throw new CommandException(MESSAGE_ALREADY_AUTHENTICATED);
        }

        if (!model.hasAccount(toLogin)) {
            throw new CommandException(MESSAGE_ACCOUNT_NOT_FOUND);
        } else { // wrong password but that is a valid username
            retrievedAccount = model.getAccount(toLogin);

            if (!retrievedAccount.getPassword().equals(toLogin.getPassword())) {
                throw new CommandException(MESSAGE_WRONG_PASSWORD);
            }
            EventsCenter.getInstance().post(new LoginEvent(retrievedAccount));
            EventsCenter.getInstance().post(new ToggleSidePanelVisibilityEvent(true));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, retrievedAccount));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LoginCommand // instanceof handles nulls
                    && toLogin.equals(((LoginCommand) other).toLogin));
    }
}
