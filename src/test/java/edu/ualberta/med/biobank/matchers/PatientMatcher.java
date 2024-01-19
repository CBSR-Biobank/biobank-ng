package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;

import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matcher;

public class PatientMatcher {

    private PatientMatcher() {
        throw new AssertionError();
    }

    public static Matcher<PatientDTO> matches(Patient expected) {
        var matchers = compose("a patient with", hasFeature("id", PatientDTO::id, equalTo(expected.getId())))
            .and(hasFeature("pnumber", PatientDTO::pnumber, equalTo(expected.getPnumber())))
            .and(
                hasFeature(
                    "createdAt",
                    PatientDTO::createdAt,
                    DateMatchers.within(1, ChronoUnit.SECONDS, expected.getCreatedAt())
                )
            )
            .and(hasFeature("specimenCount", PatientDTO::specimenCount, equalTo(Long.valueOf(sourceSpecimens(expected).size()))))
            .and(hasFeature("aliquotCount", PatientDTO::aliquotCount, equalTo(Long.valueOf(aliquots(expected).size()))))
            .and(hasFeature("studyId", PatientDTO::studyId, equalTo(expected.getStudy().getId())))
            .and(hasFeature("studyNameShort", PatientDTO::studyNameShort, equalTo(expected.getStudy().getNameShort())));

        if (!expected.getCollectionEvents().isEmpty()) {
            matchers.and(
                hasFeature(
                    "collectionEvents",
                    PatientDTO::collectionEvents,
                    CollectionEventMatcher.containsAllSummaries(expected.getCollectionEvents())
                )
            );
        }

        return matchers;
    }

    private static Set<Specimen> sourceSpecimens(Patient patient) {
        Set<Specimen> results = new HashSet<>();

        patient
            .getCollectionEvents()
            .stream()
            .forEach(ce -> {
                ce.getAllSpecimens().stream().filter(s -> s.getParentSpecimen() == null).forEach(s -> results.add(s));
            });
        return results;
    }

    private static Set<Specimen> aliquots(Patient patient) {
        Set<Specimen> results = new HashSet<>();

        patient
            .getCollectionEvents()
            .stream()
            .forEach(ce -> {
                ce.getAllSpecimens().stream().filter(s -> s.getParentSpecimen() != null).forEach(s -> results.add(s));
            });
        return results;
    }
}
