package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionEventMatcher {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventMatcher.class);

    private CollectionEventMatcher() {
        throw new AssertionError();
    }

    public static Matcher<CollectionEventDTO> matches(CollectionEvent expected) {
        var matchers = compose(
            "a collection-event with",
            hasFeature("id", CollectionEventDTO::id, equalTo(expected.getId()))
        )
            .and(hasFeature("visitNumber", CollectionEventDTO::visitNumber, equalTo(expected.getVisitNumber())))
            .and(hasFeature("status", CollectionEventDTO::status, equalTo(expected.getActivityStatus().toString())))
            .and(hasFeature("patientId", CollectionEventDTO::patientId, equalTo(expected.getPatient().getId())))
            .and(
                hasFeature(
                    "patientNumber",
                    CollectionEventDTO::patientNumber,
                    equalTo(expected.getPatient().getPnumber())
                )
            )
            .and(hasFeature("studyId", CollectionEventDTO::studyId, equalTo(expected.getPatient().getStudy().getId())))
            .and(
                hasFeature(
                    "studyNameShort",
                    CollectionEventDTO::studyNameShort,
                    equalTo(expected.getPatient().getStudy().getNameShort())
                )
            )
            .and(hasFeature("studyId", CollectionEventDTO::studyId, equalTo(expected.getPatient().getStudy().getId())))
            .and(
                hasFeature("attributes size", CollectionEventDTO::annotations, hasSize(expected.getEventAttrs().size()))
            )
            .and(hasFeature("commentCount", CollectionEventDTO::studyId, equalTo(expected.getComments().size())));

        if (!sourceSpecimens(expected).isEmpty()) {
            matchers.and(
                hasFeature(
                    "sourceSpecimens",
                    CollectionEventDTO::sourceSpecimens,
                    SourceSpecimenMatcher.containsAll(sourceSpecimens(expected))
                )
            );
        }

        return matchers;
    }

    public static Matcher<CollectionEventSummaryDTO> matchesSummary(CollectionEvent expected) {
        var matchers = compose(
            "a collection-event with",
            hasFeature("id", CollectionEventSummaryDTO::id, equalTo(expected.getId()))
        )
            .and(hasFeature("visitNumber", CollectionEventSummaryDTO::visitNumber, equalTo(expected.getVisitNumber())))
            .and(
                hasFeature(
                    "status",
                    CollectionEventSummaryDTO::status,
                    equalTo(expected.getActivityStatus().toString())
                )
            )
            .and(
                hasFeature(
                    "specimenCount",
                    CollectionEventSummaryDTO::specimenCount,
                    equalTo(Long.valueOf(sourceSpecimens(expected).size()))
                )
            );

        if (!sourceSpecimens(expected).isEmpty()) {
            matchers.and(
                hasFeature(
                    "createdAt",
                    CollectionEventSummaryDTO::createdAt,
                    DateMatchers.within(1, ChronoUnit.SECONDS, createdAt(expected))
                )
            );
        }

        if (!aliquots(expected).isEmpty()) {
            matchers.and(
                hasFeature(
                    "aliquotCount",
                    CollectionEventSummaryDTO::aliquotCount,
                    equalTo(Long.valueOf(aliquots(expected).size()))
                )
            );
        }

        return matchers;
    }

    private static List<Specimen> sourceSpecimens(CollectionEvent collectionEvent) {
        List<Specimen> results = new ArrayList<>();
        collectionEvent
            .getAllSpecimens()
            .stream()
            .filter(s -> s.getParentSpecimen() == null)
            .forEach(s -> results.add(s));
        return results;
    }

    private static List<Specimen> aliquots(CollectionEvent collectionEvent) {
        List<Specimen> results = new ArrayList<>();
        collectionEvent
            .getAllSpecimens()
            .stream()
            .filter(s -> s.getParentSpecimen() != null)
            .forEach(s -> results.add(s));
        return results;
    }

    private static Date createdAt(CollectionEvent collectionEvent) {
        return collectionEvent
            .getAllSpecimens()
            .stream()
            .filter(s -> s.getParentSpecimen() == null)
            .map(s -> s.getCreatedAt())
            .max(Date::compareTo)
            .get();
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends CollectionEventSummaryDTO>> containsAllSummaries(
        Set<CollectionEvent> items
    ) {
        List<Matcher<? super CollectionEventSummaryDTO>> matchers = new ArrayList<
            Matcher<? super CollectionEventSummaryDTO>
        >();
        for (CollectionEvent item : items) {
            matchers.add(matchesSummary(item));
        }
        return Matchers.contains(matchers);
    }
}
