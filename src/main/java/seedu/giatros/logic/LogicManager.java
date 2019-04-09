package seedu.giatros.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.giatros.commons.core.GuiSettings;
import seedu.giatros.commons.core.LogsCenter;
import seedu.giatros.logic.commands.Command;
import seedu.giatros.logic.commands.CommandResult;
import seedu.giatros.logic.commands.exceptions.CommandException;
import seedu.giatros.logic.parser.GiatrosBookParser;
import seedu.giatros.logic.parser.exceptions.ParseException;
import seedu.giatros.model.Model;
import seedu.giatros.model.ReadOnlyGiatrosBook;
import seedu.giatros.model.account.Account;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandHistory history;
    private final GiatrosBookParser giatrosBookParser;
    private boolean giatrosBookModified;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        history = new CommandHistory();
        giatrosBookParser = new GiatrosBookParser();

        // Set giatrosBookModified to true whenever the models' Giatros book is modified.
        model.getGiatrosBook().addListener(observable -> giatrosBookModified = true);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        giatrosBookModified = false;

        CommandResult commandResult;
        try {
            Command command = giatrosBookParser.parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            history.add(commandText);
        }

        if (giatrosBookModified) {
            logger.info("Giatros book modified, saving to file.");
            try {
                storage.saveGiatrosBook(model.getGiatrosBook());
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyGiatrosBook getGiatrosBook() {
        return model.getGiatrosBook();
    }

    @Override
    public ObservableList<Patient> getFilteredPatientList() {
        return model.getFilteredPatientList();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getGiatrosBookFilePath() {
        return model.getGiatrosBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyProperty<Patient> selectedPatientProperty() {
        return model.selectedPatientProperty();
    }

    @Override
    public void setSelectedPatient(Patient patient) {
        model.setSelectedPatient(patient);
    }

    @Override
    public ObservableList<Account> getFilteredAccountList() {
        return model.getFilteredAccountList();
    }
}
