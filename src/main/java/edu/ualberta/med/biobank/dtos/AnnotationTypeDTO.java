package edu.ualberta.med.biobank.dtos;

import jakarta.validation.constraints.NotNull;

public record AnnotationTypeDTO(
    @NotNull(message = "type cannot be blank")
    String type,

    @NotNull(message = "label cannot be blank")
    String label,

    boolean required,

    String[] validValues) {
}
