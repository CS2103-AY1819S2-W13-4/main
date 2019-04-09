package seedu.giatros.model;

import static java.util.Objects.requireNonNull;
import static seedu.giatros.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.giatros.commons.core.GuiSettings;
import seedu.giatros.commons.core.LogsCenter;
import seedu.giatros.model.account.Account;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.model.patient.exceptions.PatientNotFoundException;

/**
 * Represents the in-memory model of the Giatros book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedGiatrosBook versionedGiatrosBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Account> filteredAccounts;
    private final FilteredList<Patient> filteredPatients;
    private final SimpleObjectProperty<Patient> selectedPatient = new SimpleObjectProperty<>();

    /**
     * Initializes a ModelManager with the given GiatrosBook and userPrefs.
     */
    public ModelManager(ReadOnlyGiatrosBook giatrosBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(giatrosBook, userPrefs);

        logger.fine("Initializing with giatros book: " + giatrosBook + " and user prefs " + userPrefs);

        versionedGiatrosBook = new VersionedGiatrosBook(giatrosBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPatients = new FilteredList<>(versionedGiatrosBook.getPatientList());
        filteredPatients.addListener(this::ensureSelectedPatientIsValid);
        filteredAccounts = new FilteredList<>(versionedGiatrosBook.getAccountList());
    }

    public ModelManager() {
        this(new GiatrosBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getGiatrosBookFilePath() {
        return userPrefs.getGiatrosBookFilePath();
    }

    @Override
    public void setGiatrosBookFilePath(Path giatrosBookFilePath) {
        requireNonNull(giatrosBookFilePath);
        userPrefs.setGiatrosBookFilePath(giatrosBookFilePath);
    }

    //=========== GiatrosBook ================================================================================

    @Override
    public void setGiatrosBook(ReadOnlyGiatrosBook giatrosBook) {
        versionedGiatrosBook.resetData(giatrosBook);
    }

    @Override
    public ReadOnlyGiatrosBook getGiatrosBook() {
        return versionedGiatrosBook;
    }

    @Override
    public boolean hasPatient(Patient patient) {
        requireNonNull(patient);
        return versionedGiatrosBook.hasPatient(patient);
    }

    @Override
    public void deletePatient(Patient target) {
        versionedGiatrosBook.removePatient(target);
    }

    @Override
    public void addPatient(Patient patient) {
        versionedGiatrosBook.addPatient(patient);
        updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
    }

    @Override
    public void setPatient(Patient target, Patient editedPatient) {
        requireAllNonNull(target, editedPatient);

        versionedGiatrosBook.setPatient(target, editedPatient);
    }

    //=========== Filtered Patient List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Patient} backed by the internal list of
     * {@code versionedGiatrosBook}
     */
    @Override
    public ObservableList<Patient> getFilteredPatientList() {
        return filteredPatients;
    }

    @Override
    public void updateFilteredPatientList(Predicate<Patient> predicate) {
        requireNonNull(predicate);
        filteredPatients.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoGiatrosBook() {
        return versionedGiatrosBook.canUndo();
    }

    @Override
    public boolean canRedoGiatrosBook() {
        return versionedGiatrosBook.canRedo();
    }

    @Override
    public void undoGiatrosBook() {
        versionedGiatrosBook.undo();
    }

    @Override
    public void redoGiatrosBook() {
        versionedGiatrosBook.redo();
    }

    @Override
    public void commitGiatrosBook() {
        versionedGiatrosBook.commit();
    }

    @Override
    public void resetGiatrosBookVersion() {
        versionedGiatrosBook.reset();
    }

    //=========== Accounts =================================================================================

    @Override
    public ObservableList<Account> getFilteredAccountList() {
        return FXCollections.unmodifiableObservableList(filteredAccounts);
    }

    @Override
    public void addAccount(Account account) {
        versionedGiatrosBook.addAccount(account);
    }

    @Override
    public Account getAccount(Account account) {
        return versionedGiatrosBook.getAccount(account);
    }

    @Override
    public boolean hasAccount(Account account) {
        return versionedGiatrosBook.hasAccount(account);
    }

    //=========== Selected patient ===========================================================================

    @Override
    public ReadOnlyProperty<Patient> selectedPatientProperty() {
        return selectedPatient;
    }

    @Override
    public Patient getSelectedPatient() {
        return selectedPatient.getValue();
    }

    @Override
    public void setSelectedPatient(Patient patient) {
        if (patient != null && !filteredPatients.contains(patient)) {
            throw new PatientNotFoundException();
        }
        selectedPatient.setValue(patient);
    }

    /**
     * Ensures {@code selectedPatient} is a valid patient in {@code filteredPatients}.
     */
    private void ensureSelectedPatientIsValid(ListChangeListener.Change<? extends Patient> change) {
        while (change.next()) {
            if (selectedPatient.getValue() == null) {
                // null is always a valid selected patient, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPatientReplaced = change.wasReplaced() && change.getAddedSize()
                    == change.getRemovedSize() && change.getRemoved().contains(selectedPatient.getValue());
            if (wasSelectedPatientReplaced) {
                // Update selectedPatient to its new value.
                int index = change.getRemoved().indexOf(selectedPatient.getValue());
                selectedPatient.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPatientRemoved = change.getRemoved().stream()
                    .anyMatch(removedPatient -> selectedPatient.getValue().isSamePatient(removedPatient));
            if (wasSelectedPatientRemoved) {
                // Select the patient that came before it in the list,
                // or clear the selection if there is no such patient.
                selectedPatient.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedGiatrosBook.equals(other.versionedGiatrosBook)
                && userPrefs.equals(other.userPrefs)
                && filteredAccounts.equals(other.filteredAccounts)
                && filteredPatients.equals(other.filteredPatients)
                && Objects.equals(selectedPatient.get(), other.selectedPatient.get());
    }

}
