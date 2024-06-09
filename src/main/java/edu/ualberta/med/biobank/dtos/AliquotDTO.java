package edu.ualberta.med.biobank.dtos;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Tuple;

public record AliquotDTO(
    String study,
    String pnumber,
    Number visitNumber,
    String inventoryId,
    String specimenType,
    Date timeDrawn,
    BigDecimal quantity,
    String center,
    String topContainer
) {
    public static AliquotDTO fromTuple(Tuple data) {
        return new AliquotDTO(
            data.get("study", String.class),
            data.get("pnumber", String.class),
            data.get("visit_number", Number.class),
            data.get("inventory_id", String.class),
            data.get("specimen_type", String.class),
            data.get("time_drawn", Date.class),
            data.get("quantity", BigDecimal.class),
            data.get("center", String.class),
            data.get("top_container", String.class)
        );
    }
}
