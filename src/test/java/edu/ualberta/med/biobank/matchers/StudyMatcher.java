package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.ArrayMatching;
import edu.ualberta.med.biobank.domain.SourceSpecimen;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.domain.StudyEventAttr;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenTypeDTO;
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
            .and(hasFeature("status", StudyDTO::status, equalTo(expected.getActivityStatus().toString())));
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

    public static Matcher<AnnotationTypeDTO> matchesAnnotationType(StudyEventAttr expected) {
        var matchers = compose(
            "an annotation type with",
            hasFeature(
                "type",
                AnnotationTypeDTO::type,
                equalTo(expected.getGlobalEventAttr().getEventAttrType().getName())
            )
        )
            .and(hasFeature("label", AnnotationTypeDTO::label, equalTo(expected.getGlobalEventAttr().getLabel())))
            .and(hasFeature("status", AnnotationTypeDTO::status, equalTo(expected.getActivityStatus().toString())))
            .and(hasFeature("required", AnnotationTypeDTO::required, equalTo(expected.getRequired())));

        String permissible = expected.getPermissible();
        if (permissible != null && !permissible.isBlank()) {
            String[] permissibleAsArray = expected.getPermissible().split(";");
            matchers.and(
                hasFeature(
                    "validValues",
                    AnnotationTypeDTO::validValues,
                    ArrayMatching.arrayContainingInAnyOrder(permissibleAsArray)
                )
            );
        }
        return matchers;
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends AnnotationTypeDTO>> containsAttributeTypes(Study study) {
        List<Matcher<? super AnnotationTypeDTO>> matchers = new ArrayList<Matcher<? super AnnotationTypeDTO>>();
        for (StudyEventAttr attr : study.getStudyEventAttrs()) {
            matchers.add(matchesAnnotationType(attr));
        }
        return Matchers.contains(matchers);
    }

    public static Matcher<SourceSpecimenTypeDTO> matchesSourceSpecimenType(SourceSpecimen expected) {
        var matchers = compose(
            "an source specimen type with",
            hasFeature(
                "name",
                SourceSpecimenTypeDTO::name,
                equalTo(expected.getSpecimenType().getName())
            )
        )
            .and(hasFeature("nameShort", SourceSpecimenTypeDTO::nameShort, equalTo(expected.getSpecimenType().getNameShort())))
            .and(hasFeature("needOriginalVolume", SourceSpecimenTypeDTO::needOriginalVolume, equalTo(expected.getNeedOriginalVolume())));
        return matchers;
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends SourceSpecimenTypeDTO>> containsSourceSpecimenTypes(Study study) {
        List<Matcher<? super SourceSpecimenTypeDTO>> matchers = new ArrayList<Matcher<? super SourceSpecimenTypeDTO>>();
        for (SourceSpecimen source : study.getSourceSpecimens()) {
            matchers.add(matchesSourceSpecimenType(source));
        }
        return Matchers.contains(matchers);
    }
}
