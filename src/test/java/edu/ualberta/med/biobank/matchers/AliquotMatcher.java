package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;

import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class AliquotMatcher {

    private AliquotMatcher() {
        throw new AssertionError();
    }

    public static Matcher<AliquotSpecimenDTO> matches(Specimen expected) {
        return compose("an aliquot with", hasFeature("id", AliquotSpecimenDTO::id, equalTo(expected.getId())))
            .and(
                hasFeature(
                    "parentId",
                    AliquotSpecimenDTO::parentId,
                    equalTo(expected.getParentSpecimen() != null ? expected.getParentSpecimen().getId() : null)
                )
            )
            .and(hasFeature("inventoryId", AliquotSpecimenDTO::inventoryId, equalTo(expected.getInventoryId())))
            .and(
                hasFeature(
                    "specimenTypeId",
                    AliquotSpecimenDTO::specimenTypeId,
                    equalTo(expected.getSpecimenType().getId())
                )
            )
            .and(
                hasFeature(
                    "specimenTypeName",
                    AliquotSpecimenDTO::specimenTypeNameShort,
                    equalTo(expected.getSpecimenType().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "createdAt",
                    AliquotSpecimenDTO::createdAt,
                    DateMatchers.within(1, ChronoUnit.SECONDS, expected.getCreatedAt())
                )
            )
            .and(hasFeature("quantity", AliquotSpecimenDTO::quantity, Matchers.comparesEqualTo(expected.getQuantity())))
            .and(hasFeature("status", AliquotSpecimenDTO::status, equalTo(expected.getActivityStatus().toString())))
            .and(
                hasFeature(
                    "originCenterId",
                    AliquotSpecimenDTO::originCenterId,
                    equalTo(expected.getOriginInfo().getCenter().getId())
                )
            )
            .and(
                hasFeature(
                    "originCenterNameShort",
                    AliquotSpecimenDTO::originCenterNameShort,
                    equalTo(expected.getOriginInfo().getCenter().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "currentCenterId",
                    AliquotSpecimenDTO::currentCenterId,
                    equalTo(expected.getCurrentCenter().getId())
                )
            )
            .and(
                hasFeature(
                    "currentCenterShort",
                    AliquotSpecimenDTO::currentCenterNameShort,
                    equalTo(expected.getCurrentCenter().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "has comments",
                    AliquotSpecimenDTO::hasComments,
                    Matchers.not(equalTo(expected.getComments().isEmpty()))
                )
            )
            .and(
                hasFeature(
                    "position",
                    AliquotSpecimenDTO::position,
                    equalTo(
                        expected.getSpecimenPosition() != null
                            ? expected.getSpecimenPosition().getPositionString()
                            : null
                    )
                )
            )
            .and(
                hasFeature(
                    "patientNumber",
                    AliquotSpecimenDTO::patientNumber,
                    equalTo(expected.getCollectionEvent().getPatient().getPnumber())
                )
            )
            .and(
                hasFeature(
                    "studyId",
                    AliquotSpecimenDTO::studyId,
                    equalTo(expected.getCollectionEvent().getPatient().getStudy().getId())
                )
            )
            .and(
                hasFeature(
                    "studyNameShort",
                    AliquotSpecimenDTO::studyNameShort,
                    equalTo(expected.getCollectionEvent().getPatient().getStudy().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "processingEventId",
                    AliquotSpecimenDTO::processingEventId,
                    equalTo(
                        expected.getParentSpecimen() != null
                            ? expected.getParentSpecimen().getProcessingEvent().getId()
                            : null
                    )
                )
            )
            .and(
                hasFeature(
                    "worksheet",
                    AliquotSpecimenDTO::worksheet,
                    equalTo(
                        expected.getParentSpecimen().getProcessingEvent() != null
                            ? expected.getParentSpecimen().getProcessingEvent().getWorksheet()
                            : null
                    )
                )
            );
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends AliquotSpecimenDTO>> containsAll(List<Specimen> items) {
        List<Matcher<? super AliquotSpecimenDTO>> matchers = new ArrayList<Matcher<? super AliquotSpecimenDTO>>();
        for (Specimen item : items) {
            matchers.add(matches(item));
        }
        return Matchers.contains(matchers);
    }
}
