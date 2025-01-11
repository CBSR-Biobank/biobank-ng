package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import jakarta.persistence.Tuple;

public record AppLogEntryDTO(
    Integer id,
    Date   createdAt,
    String username,
    String center,
    String action,
    String patientNumber,
    String inventoryId,
    String locationLabel,
    String details,
    String type
) {
    public static AppLogEntryDTO fromTuple(Tuple data) {
        return new AppLogEntryDTO(
            data.get("id", Number.class).intValue(),
            data.get("createdAt", Date.class),
            data.get("username", String.class),
            data.get("center", String.class),
            data.get("action", String.class),
            data.get("patientNumber", String.class),
            data.get("inventoryId", String.class),
            data.get("locationLabel", String.class),
            data.get("details", String.class),
            data.get("type", String.class)
        );
    }
}
