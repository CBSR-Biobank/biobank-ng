package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;

/**
 * Used to capture specimen information when we do not know if this is a source spcecimen or an aliquot.
 */
public record SpecimenTypeDTO(
    Integer id,
    String name,
    String nameShort
) {
    public static SpecimenTypeDTO fromTuple(Tuple data) {
        return new SpecimenTypeDTO(
            data.get("id", Number.class).intValue(),
            data.get("name", String.class),
            data.get("nameShort", String.class)
        );
    }
}
