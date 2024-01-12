package edu.ualberta.med.biobank.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.SpecimenDTO;

public class SpecimenMatcher extends TypeSafeMatcher<SpecimenDTO> {

    private final Specimen specimen;

    public SpecimenMatcher(final Specimen specimen) {
        this.specimen = specimen;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("matches specimen with inventory ID=`" + specimen.getInventoryId() + "`");
    }

    @Override
    public boolean matchesSafely(final SpecimenDTO dto) {
        return dto.inventoryId().matches(specimen.getInventoryId());
    }


     // matcher method you can call on this matcher class
    public static SpecimenMatcher matchesSpecimen(final Specimen specimen) {
        return new SpecimenMatcher(specimen);
    }
}
