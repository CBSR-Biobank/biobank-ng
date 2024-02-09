package edu.ualberta.med.biobank.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientUpdateDTO(
    @NotBlank(message = "patient number cannot be blank")
    String pnumber,

    @NotNull(message = "study short name cannot be blank")
    String studyNameShort
) {}
