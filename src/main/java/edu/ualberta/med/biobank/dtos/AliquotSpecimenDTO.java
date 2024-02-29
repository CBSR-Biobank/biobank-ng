package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.Date;

public record AliquotSpecimenDTO(
    Integer id,
    Integer parentId,
    String inventoryId,
    Integer specimenTypeId,
    String specimenTypeNameShort,
    Date createdAt,
    BigDecimal quantity,
    String status,
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
    public static AliquotSpecimenDTO fromTuple(Tuple data) {
        return new AliquotSpecimenDTO(
            data.get("specimenId", Number.class).intValue(),
            data.get("parentSpecimenId", Number.class).intValue(),
            data.get("specimenInventoryId", String.class),
            data.get("specimenTypeId", Number.class).intValue(),
            data.get("specimenTypeNameShort", String.class),
            data.get("specimenCreatedAt", Date.class),
            data.get("specimenQuantity", BigDecimal.class),
            Status.fromId(data.get("specimenActivityStatusId", Integer.class)).toString(),
            data.get("originCenterId", Number.class).intValue(),
            data.get("originCenterNameShort", String.class),
            data.get("currentCenterId", Number.class).intValue(),
            data.get("currentCenterNameShort", String.class),
            data.get("hasSpecimenComments", Character.class) == 'Y',
            data.get("position", String.class),
            data.get("patientNumber", String.class),
            data.get("studyId", Number.class).intValue(),
            data.get("studyNameShort", String.class),
            data.get("processingEventId", Number.class).intValue(),
            data.get("worksheet", String.class)
        );
    }
}
