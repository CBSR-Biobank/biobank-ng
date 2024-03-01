package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.ProcessingEvent;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.Date;

public record SourceSpecimenDTO(
    Integer id,
    String inventoryId,
    Integer specimenTypeId,
    String specimenTypeNameShort,
    Date timeDrawn,
    BigDecimal quantity,
    String status,
    String pnumber,
    Integer vnumber,
    Integer originCenterId,
    String originCenterNameShort,
    Integer currentCenterId,
    String currentCenterNameShort,
    Boolean hasComments,
    String position,
    Integer processingEventId,
    String worksheet
) {
    public static SourceSpecimenDTO fromTuple(Tuple data) {
        return new SourceSpecimenDTO(
            data.get("specimenId", Number.class).intValue(),
            data.get("specimenInventoryId", String.class),
            data.get("specimenTypeId", Number.class).intValue(),
            data.get("specimenTypeNameShort", String.class),
            data.get("specimenCreatedAt", Date.class),
            data.get("specimenQuantity", BigDecimal.class),
            Status.fromId(data.get("specimenActivityStatusId", Integer.class)).toString(),
            data.get("patientNumber", String.class),
            data.get("visitNumber", Number.class).intValue(),
            data.get("originCenterId", Number.class).intValue(),
            data.get("originCenterNameShort", String.class),
            data.get("currentCenterId", Number.class).intValue(),
            data.get("currentCenterNameShort", String.class),
            data.get("hasSpecimenComments", Integer.class) == 1,
            data.get("position", String.class),
            data.get("processingEventId", Integer.class),
            data.get("worksheet", String.class)
        );
    }

    public static SourceSpecimenDTO fromSpecimen(Specimen specimen) {
        Integer peventId = null;
        String worksheet = null;

        ProcessingEvent pevent = specimen.getProcessingEvent();
        if (pevent != null) {
            peventId = pevent.getId();
            worksheet = pevent.getWorksheet();
        }

        return new SourceSpecimenDTO(
            specimen.getId(),
            specimen.getInventoryId(),
            specimen.getSpecimenType().getId(),
            specimen.getSpecimenType().getNameShort(),
            specimen.getCreatedAt(),
            specimen.getQuantity(),
            specimen.getActivityStatus().toString(),
            specimen.getCollectionEvent().getPatient().getPnumber(),
            specimen.getCollectionEvent().getVisitNumber(),
            specimen.getOriginInfo().getCenter().getId(),
            specimen.getOriginInfo().getCenter().getNameShort(),
            specimen.getCurrentCenter().getId(),
            specimen.getCurrentCenter().getNameShort(),
            !specimen.getComments().isEmpty(),
            specimen.getSpecimenPosition() != null ? specimen.getSpecimenPosition().getPositionString() : null,
            peventId,
            worksheet
        );
    }
}
