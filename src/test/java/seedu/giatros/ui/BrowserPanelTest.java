package seedu.giatros.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.giatros.testutil.TypicalPatients.ALICE;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import seedu.giatros.model.patient.Patient;

public class BrowserPanelTest extends GuiUnitTest {
    private SimpleObjectProperty<Patient> selectedPatient = new SimpleObjectProperty<>();
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        //assertEquals(BrowserPanel.DEFAULT_PAGE, browserPanelHandle.getLoadedUrl());

        // associated web page of a patient
        guiRobot.interact(() -> selectedPatient.set(ALICE));
        URL expectedPatientUrl = new URL(BrowserPanel.SEARCH_PAGE_URL + ALICE.getName().fullName
                .replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedPatientUrl, browserPanelHandle.getLoadedUrl());
    }
}
