package seedu.giatros.ui.account;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.giatros.commons.core.LogsCenter;
import seedu.giatros.commons.events.ui.accounts.LoginEvent;
import seedu.giatros.commons.events.ui.accounts.LogoutEvent;
import seedu.giatros.ui.UiPart;

/**
 * A ui for the {@code Username} label that is displayed at the header of the application.
 */
public class UsernameDisplay extends UiPart<Region> {

    private static final String ACCOUNT_STATUS_USER = "Guest";
    private static final String ACCOUNT_STATUS = "%s";

    private static final Logger logger = LogsCenter.getLogger(UsernameDisplay.class);
    private static final String FXML = "UsernameDisplay.fxml";

    @FXML
    private Label usernameDisplay;

    public UsernameDisplay() {
        super(FXML);
        usernameDisplay.setText(ACCOUNT_STATUS_USER);
        registerAsAnEventHandler(this); // To trigger @Subscribe
    }

    private void setUsername(String message) {
        usernameDisplay.setText(message);
    }

    @Subscribe
    private void handleLoginEvent(LoginEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setUsername(String.format(ACCOUNT_STATUS, event.username.toString().toUpperCase()));
    }

    @Subscribe
    private void handleLogoutEvent(LogoutEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setUsername(ACCOUNT_STATUS_USER);
    }

    public String getAccountStatusUser() {
        return usernameDisplay.getText();
    }
}
