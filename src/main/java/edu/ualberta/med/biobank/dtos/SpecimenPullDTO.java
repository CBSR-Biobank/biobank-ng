package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.SpecimenPull;
import java.util.Date;

public record SpecimenPullDTO(
    String pnumber,
    String inventoryId,
    Date dateDrawn,
    String specimenType,
    String location,
    String activityStatus
) {
    public static SpecimenPullDTO fromSpecimenPull(SpecimenPull pull) {
        return new SpecimenPullDTO(
            pull.pnumber(),
            pull.inventoryId(),
            pull.dateDrawn(),
            pull.specimenType(),
            pull.location(),
            pull.activityStatus().toString()
        );
    }
}
