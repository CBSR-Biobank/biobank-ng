package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;
import java.util.Date;

public record CollectionEventSummaryDTO(
    Integer id,
    Integer visitNumber,
    Date createdAt,
    String status,
    Long specimenCount,
    Long aliquotCount
) {
    public static CollectionEventSummaryDTO fromTuple(Tuple data) {
        return new CollectionEventSummaryDTO(
            data.get("id", Integer.class),
            data.get("visitNumber", Integer.class),
            data.get("createdAt", Date.class),
            Status.fromId(data.get("ACTIVITY_STATUS_ID", Integer.class)).getName(),
            data.get("specimenCount", Long.class),
            data.get("aliquotCount", Long.class)
        );
    }
}
