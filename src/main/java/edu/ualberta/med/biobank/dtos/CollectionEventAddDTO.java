package edu.ualberta.med.biobank.dtos;

import jakarta.validation.constraints.Min;

public record CollectionEventAddDTO(
    @Min(value = 1, message = "visit number must be greater than 0")
    Integer vnumber
) {}
