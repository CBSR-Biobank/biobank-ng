package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;

public record StudyNameDTO(Integer id, String name, String nameShort) {
    public static StudyNameDTO fromTuple(Tuple data) {
        return new StudyNameDTO(
            data.get("id", Number.class).intValue(),
            data.get("name", String.class),
            data.get("nameShort", String.class)
);
    }
}
