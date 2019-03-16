package seedu.giatros;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.giatros.commons.core.Config;
import seedu.giatros.commons.core.GuiSettings;
import seedu.giatros.commons.exceptions.DataConversionException;
import seedu.giatros.model.AddressBook;
import seedu.giatros.model.Model;
import seedu.giatros.model.ModelManager;
import seedu.giatros.model.ReadOnlyAddressBook;
import seedu.giatros.model.UserPrefs;
import seedu.giatros.storage.JsonAddressBookStorage;
import seedu.giatros.storage.UserPrefsStorage;
import seedu.giatros.testutil.TestUtil;
import systemtests.ModelHelper;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final Path SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.json");

    protected static final Path DEFAULT_PREF_FILE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    protected Supplier<ReadOnlyAddressBook> initialDataSupplier = () -> null;
    protected Path saveFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyAddressBook> initialDataSupplier, Path saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(saveFileLocation);
            try {
                jsonAddressBookStorage.saveAddressBook(initialDataSupplier.get());
            } catch (IOException ioe) {
                throw new AssertionError(ioe);
            }
        }
    }

    @Override
    protected Config initConfig(Path configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.setGuiSettings(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        userPrefs.setAddressBookFilePath(saveFileLocation);
        return userPrefs;
    }

    /**
     * Returns a defensive copy of the giatros book data stored inside the storage file.
     */
    public AddressBook readStorageAddressBook() {
        try {
            return new AddressBook(storage.readAddressBook().get());
        } catch (DataConversionException dce) {
            throw new AssertionError("Data is not in the AddressBook format.", dce);
        } catch (IOException ioe) {
            throw new AssertionError("Storage file cannot be found.", ioe);
        }
    }

    /**
     * Returns the file path of the storage file.
     */
    public Path getStorageSaveLocation() {
        return storage.getAddressBookFilePath();
    }

    /**
     * Returns a defensive copy of the model.
     */
    public Model getModel() {
        Model copy = new ModelManager((model.getAddressBook()), new UserPrefs());
        ModelHelper.setFilteredList(copy, model.getFilteredPersonList());
        return copy;
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
