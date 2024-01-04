package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.Date;

public record SourceSpecimenDTO(
    Integer id,
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
            Status.fromId(data.get("specimenActivityStatusId", Integer.class)).getName(),
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
}
