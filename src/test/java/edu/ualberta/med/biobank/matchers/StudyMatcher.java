package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import org.hamcrest.Matcher;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.StudyDTO;

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
}
