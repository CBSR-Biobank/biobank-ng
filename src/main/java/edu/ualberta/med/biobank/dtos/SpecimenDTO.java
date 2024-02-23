package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Used to capture specimen information when we do not know if this is a source spcecimen or an aliquot.
 */
public record SpecimenDTO(
    Integer id,
    Integer parentId,
    String inventoryId,
    Date createdAt,
    BigDecimal quantity,
    String status,
    Integer specimenTypeId,
    String specimenTypeNameShort,
    Integer originCenterId,
    String originCenterNameShort,
    Integer currentCenterId,
    String currentCenterNameShort,
    Boolean hasComments,
    String position,
    String patientNumber,
    Integer studyId,
    String studyNameShort,
    Integer processingEventId,
    String worksheet
) {
    public static SpecimenDTO fromTuple(Tuple data) {
        return new SpecimenDTO(
            data.get("specimenId", Number.class).intValue(),
            data.get("parentSpecimenId", Integer.class),
            data.get("specimenInventoryId", String.class),
            data.get("specimenCreatedAt", Date.class),
            data.get("specimenQuantity", BigDecimal.class),
            Status.fromId(data.get("specimenActivityStatusId", Integer.class)).getName(),
            data.get("specimenTypeId", Number.class).intValue(),
            data.get("specimenTypeNameShort", String.class),
            data.get("originCenterId", Number.class).intValue(),
            data.get("originCenterNameShort", String.class),
            data.get("currentCenterId", Number.class).intValue(),
            data.get("currentCenterNameShort", String.class),
            data.get("hasSpecimenComments", Character.class) == 'Y',
            data.get("position", String.class),
            data.get("patientNumber", String.class),
            data.get("studyId", Number.class).intValue(),
            data.get("studyNameShort", String.class),
            data.get("processingEventId", Integer.class),
            data.get("worksheet", String.class)
        );
    }

    public boolean isSourceSpecimen() {
        return parentId == null;
    }
}
