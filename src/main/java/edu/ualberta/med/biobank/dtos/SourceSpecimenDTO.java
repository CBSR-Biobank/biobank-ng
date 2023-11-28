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
    Date timeDrawn,
    BigDecimal quantity,
    String status,
    Integer originCenterId,
    String originCenterNameShort,
    Integer currentCenterId,
    String currentCenterNameShort,
    Boolean hasComments
) {
    public static SourceSpecimenDTO fromTuple(Tuple data) {
        return new SourceSpecimenDTO(
            data.get("specimenId", Number.class).intValue(),
            data.get("inventoryId", String.class),
            data.get("specimenTypeId", Number.class).intValue(),
            data.get("specimenTypeNameShort", String.class),
            data.get("timeDrawn", Date.class),
            data.get("quantity", BigDecimal.class),
            Status.fromId(data.get("ACTIVITY_STATUS_ID", Integer.class)).getName(),
            data.get("originCenterId", Number.class).intValue(),
            data.get("originCenterNameShort", String.class),
            data.get("currentCenterId", Number.class).intValue(),
            data.get("currentCenterNameShort", String.class),
            data.get("hasSpecimenComments", Integer.class) == 1
        );
    }
}
