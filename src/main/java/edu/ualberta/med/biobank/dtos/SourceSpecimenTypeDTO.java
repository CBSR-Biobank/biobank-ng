package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotNull;

public record SourceSpecimenTypeDTO(

    @NotNull(message = "name cannot be blank")
    String name,

    @NotNull(message = "short name cannot be blank")
    String nameShort,

    @NotNull(message = "needOriginalVolume cannot be blank")
    Boolean needOriginalVolume) {

    public static SourceSpecimenTypeDTO fromTuple(Tuple data) {
        return new SourceSpecimenTypeDTO(
            data.get("name", String.class),
            data.get("nameShort", String.class),
            data.get("needOriginalVolume", Boolean.class)
        );
    }
}
