package seedu.giatros.logic.commands;

import static seedu.giatros.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.giatros.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.giatros.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.giatros.commons.core.Messages;
import seedu.giatros.commons.core.index.Index;
import seedu.giatros.logic.CommandHistory;
import seedu.giatros.logic.commands.exceptions.CommandException;
import seedu.giatros.model.Model;
import seedu.giatros.model.allergy.Allergy;
import seedu.giatros.model.patient.Patient;

/**
 * Adds an allergy to an existing patient in the giatros book.
 */
public class AddallCommand extends Command {

    public static final String COMMAND_WORD = "addall";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the allergy to the patient identified "
            + "by the index number used in the patient listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ALLERGY + "[ALLERGY]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ALLERGY + "ibuprofen.";

    public static final String MESSAGE_ADD_ALLERGY_SUCCESS = "Added allergy to Patient: %1$s";
    public static final String MESSAGE_ADD_ALLERGY_FAILURE = "Such allergy is already associated with Patient: %1$s";
    public static final String MESSAGE_INCORRECT_ALLERGY = "At least one allergy must be provided";

    private Index index;
    private Set<Allergy> allergies;

    public AddallCommand(Index index, Set<Allergy> allergies) {
        requireAllNonNull(index, allergies);

        this.index = index;
        this.allergies = allergies;
    }

    public AddallCommand(Index index, Allergy allergy) {
        requireAllNonNull(index, allergy);

        Set<Allergy> allergies = new HashSet<>();
        allergies.add(allergy);

        this.index = index;
        this.allergies = allergies;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());

        Set<Allergy> newAllergy = new HashSet<>();
        newAllergy.addAll(patientToEdit.getAllergies());
        newAllergy.addAll(allergies);

        Patient editedPatient = new Patient(patientToEdit.getName(), patientToEdit.getPhone(), patientToEdit.getEmail(),
                patientToEdit.getAddress(), newAllergy);

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        model.commitGiatrosBook();

        if (!editedPatient.equals(patientToEdit)) {
            return new CommandResult(generateSuccessMessage(editedPatient));
        }
        else {
            return new CommandResult(generateFailureMessage(editedPatient));
        }
    }

    /**
     * Generates a command execution success message when the allergy has been added to {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        String message = MESSAGE_ADD_ALLERGY_SUCCESS;
        return String.format(message, patientToEdit);
    }

    /**
     * Generates a command execution failure message when the allergy is not added to {@code patientToEdit}.
     */
    private String generateFailureMessage(Patient patientToEdit) {
        String message = MESSAGE_ADD_ALLERGY_FAILURE;
        return String.format(message, patientToEdit);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddallCommand)) {
            return false;
        }

        // state check
        AddallCommand e = (AddallCommand) other;
        return index.equals(e.index)
                && allergies.equals(e.allergies);
    }

}
