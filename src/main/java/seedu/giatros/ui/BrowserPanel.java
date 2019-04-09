package seedu.giatros.ui;

import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import seedu.giatros.MainApp;
import seedu.giatros.commons.core.LogsCenter;
import seedu.giatros.model.patient.Patient;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String SEARCH_PAGE_URL = "https://se-education.org/dummy-search-page/?name=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    @FXML
    private VBox itemDetailPane;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    private void loadPatientPage(Patient patient) {
        loadPage(SEARCH_PAGE_URL + patient.getName().fullName);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        //loadPage(DEFAULT_PAGE.toExternalForm());
        URL dPage = MainApp.class.getResource("/images/Ui.png");

        itemDetailPane.setBackground(
                new Background(
                        new BackgroundImage(
                                new Image(dPage.toExternalForm()),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)
                )
        );
        setItemVisibility(false);
    }

    private void setItemVisibility(Boolean show) {
        //nothing for now
    }


}
