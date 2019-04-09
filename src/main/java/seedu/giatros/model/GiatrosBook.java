package seedu.giatros.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.giatros.commons.util.InvalidationListenerManager;
import seedu.giatros.model.account.Account;
import seedu.giatros.model.account.UniqueAccountList;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.model.patient.UniquePatientList;

/**
 * Wraps all data at the Giatros-book level
 * Duplicates are not allowed (by .isSamePatient comparison)
 */
public class GiatrosBook implements ReadOnlyGiatrosBook {

    private final UniquePatientList patients;
    private final UniqueAccountList accounts;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        patients = new UniquePatientList();
        accounts = new UniqueAccountList();
    }

    public GiatrosBook() {}

    /**
     * Creates an GiatrosBook using the Patients in the {@code toBeCopied}
     */
    public GiatrosBook(ReadOnlyGiatrosBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the patient list with {@code patients}.
     * {@code patients} must not contain duplicate patients.
     */
    public void setPatients(List<Patient> patients) {
        this.patients.setPatients(patients);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code GiatrosBook} with {@code newData}.
     */
    public void resetData(ReadOnlyGiatrosBook newData) {
        requireNonNull(newData);

        setPatients(newData.getPatientList());
    }

    //// patient-level operations

    /**
     * Returns true if a patient with the same identity as {@code patient} exists in the Giatros book.
     */
    public boolean hasPatient(Patient patient) {
        requireNonNull(patient);
        return patients.contains(patient);
    }

    /**
     * Adds a patient to the Giatros book.
     * The patient must not already exist in the Giatros book.
     */
    public void addPatient(Patient p) {
        patients.add(p);
        indicateModified();
    }

    /**
     * Replaces the given patient {@code target} in the list with {@code editedPatient}.
     * {@code target} must exist in the Giatros book.
     * The patient identity of {@code editedPatient} must not be the same as another existing patient in the Giatros
     * book.
     */
    public void setPatient(Patient target, Patient editedPatient) {
        requireNonNull(editedPatient);

        patients.setPatient(target, editedPatient);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code GiatrosBook}.
     * {@code key} must exist in the Giatros book.
     */
    public void removePatient(Patient key) {
        patients.remove(key);
        indicateModified();
    }

    /**
     * Returns true if an account with the same identity as {@code Account} exists in the Giatros book.
     */
    public boolean hasAccount(Account account) {
        return accounts.contains(account);
    }

    /**
     * Returns the {@code Account} of the given {@code account}. That is, the account in storage is retrieved that
     * matches the {@code account} input given by the user.
     */
    public Account getAccount(Account account) {
        return accounts.get(account);
    }

    /**
     * Adds an account to the account record. The account must not already exist in the account record.
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the Giatros book has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return String.valueOf(accounts.asUnmodifiableObservableList().size() + " accounts\n"
                + patients.asUnmodifiableObservableList().size() + " patients");
        // TODO: refine later
    }

    @Override
    public ObservableList<Patient> getPatientList() {
        return patients.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Account> getAccountList() {
        return accounts.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return accounts.equals(((GiatrosBook) other).accounts)
                && patients.equals(((GiatrosBook) other).patients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts, patients);
    }
}
