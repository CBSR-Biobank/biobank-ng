package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;

public class SourceSpecimenMatcher {

    private SourceSpecimenMatcher() {
        throw new AssertionError();
    }

    public static Matcher<SourceSpecimenDTO> matches(Specimen expected) {
        return compose("a source specimen with", hasFeature("id", SourceSpecimenDTO::id, equalTo(expected.getId())))
            .and(hasFeature("inventoryId", SourceSpecimenDTO::inventoryId, equalTo(expected.getInventoryId())))
            .and(
                hasFeature(
                    "specimenTypeId",
                    SourceSpecimenDTO::specimenTypeId,
                    equalTo(expected.getSpecimenType().getId())
                )
            )
            .and(
                hasFeature(
                    "specimenTypeName",
                    SourceSpecimenDTO::specimenTypeNameShort,
                    equalTo(expected.getSpecimenType().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "createdAt",
                    SourceSpecimenDTO::timeDrawn,
                    DateMatchers.within(1, ChronoUnit.SECONDS, expected.getCreatedAt())
                )
            )
            .and(hasFeature("quantity", SourceSpecimenDTO::quantity, equalTo(expected.getQuantity())))
            .and(hasFeature("status", SourceSpecimenDTO::status, equalTo(expected.getActivityStatus().toString())))
            .and(hasFeature("pnumber", SourceSpecimenDTO::pnumber, equalTo(expected.getCollectionEvent().getPatient().getPnumber())))
            .and(hasFeature("vnumber", SourceSpecimenDTO::vnumber, equalTo(expected.getCollectionEvent().getVisitNumber())))
            .and(
                hasFeature(
                    "originCenterId",
                    SourceSpecimenDTO::originCenterId,
                    equalTo(expected.getOriginInfo().getCenter().getId())
                )
            )
            .and(
                hasFeature(
                    "originCenterNameShort",
                    SourceSpecimenDTO::originCenterNameShort,
                    equalTo(expected.getOriginInfo().getCenter().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "currentCenterId",
                    SourceSpecimenDTO::currentCenterId,
                    equalTo(expected.getCurrentCenter().getId())
                )
            )
            .and(
                hasFeature(
                    "currentCenterShort",
                    SourceSpecimenDTO::currentCenterNameShort,
                    equalTo(expected.getCurrentCenter().getNameShort())
                )
            )
            .and(
                hasFeature(
                    "has comments",
                    SourceSpecimenDTO::hasComments,
                    Matchers.not(equalTo(expected.getComments().isEmpty()))
                )
            )
            .and(
                hasFeature(
                    "position",
                    SourceSpecimenDTO::position,
                    equalTo(expected.getSpecimenPosition() != null ? expected.getSpecimenPosition().getPositionString() : null)
                )
            )
            .and(
                hasFeature(
                    "processingEventId",
                    SourceSpecimenDTO::processingEventId,
                    equalTo(expected.getProcessingEvent() != null ? expected.getProcessingEvent().getId() : null)
                )
            )
            .and(
                hasFeature(
                    "worksheet",
                    SourceSpecimenDTO::worksheet,
                    equalTo(expected.getProcessingEvent() != null ? expected.getProcessingEvent().getWorksheet() : null)
                )
            );
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends SourceSpecimenDTO>> containsAll(List<Specimen> items) {
        List<Matcher<? super SourceSpecimenDTO>> matchers = new ArrayList<Matcher<? super SourceSpecimenDTO>>();
        for (Specimen item : items) {
            matchers.add(matches(item));
        }
        return Matchers.contains(matchers);
    }
}
