package seedu.giatros.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.giatros.commons.core.GuiSettings;
import seedu.giatros.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

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
     * Returns the user prefs' giatros book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' giatros book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces giatros book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the giatros book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the giatros book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the giatros book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the giatros book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the giatros book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if the model has previous giatros book states to restore.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if the model has undone giatros book states to restore.
     */
    boolean canRedoAddressBook();

    /**
     * Restores the model's giatros book to its previous state.
     */
    void undoAddressBook();

    /**
     * Restores the model's giatros book to its previously undone state.
     */
    void redoAddressBook();

    /**
     * Saves the current giatros book state for undo/redo.
     */
    void commitAddressBook();

    /**
     * Selected person in the filtered person list.
     * null if no person is selected.
     */
    ReadOnlyProperty<Person> selectedPersonProperty();

    /**
     * Returns the selected person in the filtered person list.
     * null if no person is selected.
     */
    Person getSelectedPerson();

    /**
     * Sets the selected person in the filtered person list.
     */
    void setSelectedPerson(Person person);
}
