package seedu.giatros.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.giatros.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.giatros.logic.parser.CliSyntax.PREFIX_ALLERGY;

import java.util.Set;

import seedu.giatros.commons.core.index.Index;
import seedu.giatros.commons.exceptions.IllegalValueException;
import seedu.giatros.logic.commands.AddallCommand;
import seedu.giatros.logic.parser.exceptions.ParseException;
import seedu.giatros.model.allergy.Allergy;

/**
 * Parses input arguments and creates a new {@code AddallCommand} object.
 */
public class AddallCommandParser implements Parser<AddallCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddallCommand
     * and returns an AddallCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddallCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index index;
        Set<Allergy> allergies;

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ALLERGY);
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException exc) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddallCommand.MESSAGE_USAGE), exc);
        }

        if (!argMultimap.getValue(PREFIX_ALLERGY).isPresent()) {
            throw new ParseException(AddallCommand.MESSAGE_INCORRECT_ALLERGY);
        }

        allergies = ParserUtil.parseAllergies(argMultimap.getAllValues(PREFIX_ALLERGY));

        return new AddallCommand(index, allergies);
    }

}
