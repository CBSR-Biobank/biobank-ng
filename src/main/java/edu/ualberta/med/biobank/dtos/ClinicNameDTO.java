package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;

public record ClinicNameDTO(Integer id, String name, String nameShort) {
    public static ClinicNameDTO fromTuple(Tuple data) {
        return new ClinicNameDTO(
            data.get("id", Number.class).intValue(),
            data.get("name", String.class),
            data.get("nameShort", String.class)
        );
    }
}
