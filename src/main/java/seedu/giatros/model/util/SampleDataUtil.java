package seedu.giatros.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.giatros.model.GiatrosBook;
import seedu.giatros.model.ReadOnlyGiatrosBook;
import seedu.giatros.model.patient.Address;
import seedu.giatros.model.patient.Allergy;
import seedu.giatros.model.patient.Email;
import seedu.giatros.model.patient.Name;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.model.patient.Phone;
import seedu.giatros.model.tag.Tag;

/**
 * Contains utility methods for populating {@code GiatrosBook} with sample data.
 */
public class SampleDataUtil {

    public static final Allergy EMPTY_ALLERGY = new Allergy("");

    public static Patient[] getSamplePatients() {
        return new Patient[] {
            new Patient(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), EMPTY_ALLERGY,
                getTagSet("friends")),
            new Patient(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), EMPTY_ALLERGY,
                getTagSet("colleagues", "friends")),
            new Patient(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), EMPTY_ALLERGY,
                getTagSet("neighbours")),
            new Patient(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), EMPTY_ALLERGY,
                getTagSet("family")),
            new Patient(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), EMPTY_ALLERGY,
                getTagSet("classmates")),
            new Patient(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), EMPTY_ALLERGY,
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyGiatrosBook getSampleGiatrosBook() {
        GiatrosBook sampleAb = new GiatrosBook();
        for (Patient samplePatient : getSamplePatients()) {
            sampleAb.addPatient(samplePatient);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}