package seedu.giatros.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.giatros.commons.core.GuiSettings;
import seedu.giatros.model.account.Account;
import seedu.giatros.model.patient.Patient;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Patient> PREDICATE_SHOW_ALL_PATIENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' Giatros book file path.
     */
    Path getGiatrosBookFilePath();

    /**
     * Sets the user prefs' Giatros book file path.
     */
    void setGiatrosBookFilePath(Path giatrosBookFilePath);

    /**
     * Replaces Giatros book data with the data in {@code giatrosBook}.
     */
    void setGiatrosBook(ReadOnlyGiatrosBook giatrosBook);

    /** Returns the GiatrosBook */
    ReadOnlyGiatrosBook getGiatrosBook();

    /**
     * Returns true if a patient with the same identity as {@code patient} exists in the Giatros book.
     */
    boolean hasPatient(Patient patient);

    /**
     * Deletes the given patient.
     * The patient must exist in the Giatros book.
     */
    void deletePatient(Patient target);

    /**
     * Adds the given patient.
     * {@code patient} must not already exist in the Giatros book.
     */
    void addPatient(Patient patient);

    /**
     * Replaces the given patient {@code target} with {@code editedPatient}.
     * {@code target} must exist in the Giatros book.
     * The patient identity of {@code editedPatient} must not be the same as another existing patient in the Giatros
     * book.
     */
    void setPatient(Patient target, Patient editedPatient);

    /** Returns an unmodifiable view of the filtered patient list */
    ObservableList<Patient> getFilteredPatientList();

    /**
     * Updates the filter of the filtered patient list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPatientList(Predicate<Patient> predicate);

    /**
     * Returns true if the model has previous Giatros book states to restore.
     */
    boolean canUndoGiatrosBook();

    /**
     * Returns true if the model has undone Giatros book states to restore.
     */
    boolean canRedoGiatrosBook();

    /**
     * Restores the model's Giatros book to its previous state.
     */
    void undoGiatrosBook();

    /**
     * Restores the model's Giatros book to its previously undone state.
     */
    void redoGiatrosBook();

    /**
     * Saves the current Giatros book state for undo/redo.
     */
    void commitGiatrosBook();

    /**
     * Resets the version of the GiatrosBook
     */
    void resetGiatrosBookVersion();

    /**
     * Selected patient in the filtered patient list.
     * null if no patient is selected.
     */
    ReadOnlyProperty<Patient> selectedPatientProperty();

    /**
     * Returns the selected patient in the filtered patient list.
     * null if no patient is selected.
     */
    Patient getSelectedPatient();

    /**
     * Sets the selected patient in the filtered patient list.
     */
    void setSelectedPatient(Patient patient);

    /**
     * Adds the given account. {@code account} must not already exist in the account storage.
     *
     * @param account to be added.
     */
    void addAccount(Account account);

    /**
     * Retrieve the account. {@code account} must already exist in the account storage.
     *
     * @param account to retrieve.
     */
    Account getAccount(Account account);

    /**
     * Returns true if an account with the same identity as {@code Account} exists in the giatros book.
     */
    boolean hasAccount(Account account);

    /** Returns an unmodifiable view of the filtered account list */
    ObservableList<Account> getFilteredAccountList();
}
