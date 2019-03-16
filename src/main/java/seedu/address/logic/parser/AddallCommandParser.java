package seedu.giatros.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.giatros.commons.core.index.Index;
import seedu.giatros.logic.commands.AddallCommand;
import seedu.giatros.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddallCommand object
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
        String allergyString;

        // Implement a simple parser locally first
        String delims = "[ ]+";
        String[] tokens = args.trim().split(delims);
        System.out.println(tokens[0]);

        index = Index.fromOneBased(Integer.valueOf(tokens[0])); // The first argument is the index
        allergyString = tokens[1]; // The second argument is the allergyString

        if (!(allergyString.contains("a/"))) {
            throw new ParseException("Invalid format");
        }


        allergyString = allergyString.substring(2); // Trim off the tag a/ in the beginning

        return new AddallCommand(index, allergyString);
    }

}
