package seedu.giatros.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.giatros.commons.core.LogsCenter;
import seedu.giatros.commons.exceptions.IllegalValueException;
import seedu.giatros.model.GiatrosBook;
import seedu.giatros.model.ReadOnlyGiatrosBook;
import seedu.giatros.model.account.Account;
import seedu.giatros.model.patient.Patient;

/**
 * An Immutable GiatrosBook that is serializable to JSON format.
 */
@JsonRootName(value = "giatrosbook")
public class JsonSerializableGiatrosBook {

    private static final Logger logger = LogsCenter.getLogger(JsonGiatrosBookStorage.class);

    public static final String MESSAGE_DUPLICATE_ACCOUNT = "Account list contains duplicate username(s).";
    public static final String MESSAGE_DUPLICATE_PATIENT = "Patients list contains duplicate patient(s).";

    private final List<JsonAdaptedPatient> patients = new ArrayList<>();
    private final List<JsonAdaptedAccount> accounts = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableGiatrosBook} with the given patients.
     */
    @JsonCreator
    public JsonSerializableGiatrosBook(@JsonProperty("patients") List<JsonAdaptedPatient> patients,
                                       @JsonProperty("accounts") List<JsonAdaptedAccount> accounts) {
        this.patients.addAll(patients);
        this.accounts.addAll(accounts);
    }

    /**
     * Converts a given {@code ReadOnlyGiatrosBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableGiatrosBook}.
     */
    public JsonSerializableGiatrosBook(ReadOnlyGiatrosBook source) {
        patients.addAll(source.getPatientList().stream().map(JsonAdaptedPatient::new).collect(Collectors.toList()));
        accounts.addAll(source.getAccountList().stream().map(JsonAdaptedAccount::new).collect(Collectors.toList()));
    }

    /**
     * Converts this Giatros book into the model's {@code GiatrosBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public GiatrosBook toModelType() throws IllegalValueException {
        GiatrosBook giatrosBook = new GiatrosBook();

        for (JsonAdaptedPatient jsonAdaptedPatient : patients) {
            Patient patient = jsonAdaptedPatient.toModelType();
            if (giatrosBook.hasPatient(patient)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PATIENT);
            }
            giatrosBook.addPatient(patient);
        }
        for (JsonAdaptedAccount jsonAdaptedAccount : accounts) {
            Account account = jsonAdaptedAccount.toModelType();
            if (giatrosBook.hasAccount(account)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ACCOUNT);
            }
            giatrosBook.addAccount(account);
        }

        return giatrosBook;
    }


}
