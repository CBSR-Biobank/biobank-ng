package edu.ualberta.med.biobank.domain;

import java.util.Date;

public record SpecimenPull(
    String pnumber,
    String inventoryId,
    Date dateDrawn,
    String specimenType,
    String location,
    Status activityStatus
) {}
