package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import edu.ualberta.med.biobank.domain.Clinic;
import edu.ualberta.med.biobank.dtos.ClinicDTO;
import edu.ualberta.med.biobank.dtos.ClinicNameDTO;

public class ClinicMatcher {

    private ClinicMatcher() {
        throw new AssertionError();
    }

    public static Matcher<ClinicDTO> matches(Clinic expected) {
        return compose("a clinic with", hasFeature("id", ClinicDTO::id, equalTo(expected.getId())))
            .and(hasFeature("name", ClinicDTO::name, equalTo(expected.getName())))
            .and(hasFeature("nameShort", ClinicDTO::nameShort, equalTo(expected.getNameShort())))
            .and(hasFeature("status", ClinicDTO::status, equalTo(expected.getActivityStatus().toString())));
    }

    public static Matcher<ClinicNameDTO> matchesName(Clinic expected) {
        return compose("a clinic with", hasFeature("id", ClinicNameDTO::id, equalTo(expected.getId())))
            .and(hasFeature("name", ClinicNameDTO::name, equalTo(expected.getName())))
            .and(hasFeature("nameShort", ClinicNameDTO::nameShort, equalTo(expected.getNameShort())));
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends ClinicNameDTO>> containsNames(List<Clinic> items) {
        List<Matcher<? super ClinicNameDTO>> matchers = new ArrayList<Matcher<? super ClinicNameDTO>>();
        for (Clinic item : items) {
            matchers.add(matchesName(item));
        }
        return Matchers.contains(matchers);
    }
}
