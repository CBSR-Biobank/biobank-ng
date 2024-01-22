package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientCreateDTO(
    @NotBlank(message = "patient number cannot be blank")
    String pnumber,

    @NotNull(message = "created-at cannot be null")
    Date createdAt,

    @NotNull(message = "study short name cannot be blank")
    String studyNameShort
) {}
