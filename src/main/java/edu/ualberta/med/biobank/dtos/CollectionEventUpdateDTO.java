package edu.ualberta.med.biobank.dtos;

import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CollectionEventUpdateDTO(
    @Min(value = 1, message = "visit number must be greater than 0")
    @NotNull(message = "visit number cannot be empty")
    Integer vnumber,

    @NotNull(message = "status cannot be blank")
    String status,

    List<AnnotationDTO> annotations
) {}
