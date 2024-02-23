package edu.ualberta.med.biobank.dtos;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SourceSpecimenAddDTO(
    @NotBlank(message = "inventory ID cannot be blank")
    String inventoryId,

    @NotBlank(message = "specimen type name short cannot be blank")
    String specimenTypeNameShort,

    @NotNull(message = "time drawn cannot be blank")
    Date timeDrawn,

    @Min(value = 0, message = "quantity must be greater than 0")
    BigDecimal quantity,

    @NotBlank(message = "status cannot be blank")
    String status,

    @NotBlank(message = "patient number cannot be blank")
    String pnumber,

    @NotNull(message = "visit number cannot be null")
    @Min(value = 1, message = "visit number must be greater than 0")
    Integer vnumber,

    @NotBlank(message = "origin center name short cannot be blank")
    String originCenterNameShort
) {
}
