package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.dtos.StudyNameDTO;

public class StudyMatcher {

    private StudyMatcher() {
        throw new AssertionError();
    }

    public static Matcher<StudyDTO> matches(Study expected) {
        return compose("a study with", hasFeature("id", StudyDTO::id, equalTo(expected.getId())))
            .and(hasFeature("name", StudyDTO::name, equalTo(expected.getName())))
            .and(hasFeature("nameShort", StudyDTO::nameShort, equalTo(expected.getNameShort())))
            .and(
                hasFeature(
                    "status",
                    StudyDTO::status,
                    equalTo(expected.getActivityStatus().toString())
                )
            );
    }

    public static Matcher<StudyNameDTO> matchesName(Study expected) {
        return compose("a study with", hasFeature("id", StudyNameDTO::id, equalTo(expected.getId())))
            .and(hasFeature("name", StudyNameDTO::name, equalTo(expected.getName())))
            .and(hasFeature("nameShort", StudyNameDTO::nameShort, equalTo(expected.getNameShort())));
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends StudyNameDTO>> containsNames(List<Study> items) {
        List<Matcher<? super StudyNameDTO>> matchers = new ArrayList<Matcher<? super StudyNameDTO>>();
        for (Study item : items) {
            matchers.add(matchesName(item));
        }
        return Matchers.contains(matchers);
    }
}
