package edu.ualberta.med.biobank.dtos;

import java.util.Date;

public record PatientCreateDTO(
    String pnumber,
    Date createdAt,
    String studyNameShort
) {}
