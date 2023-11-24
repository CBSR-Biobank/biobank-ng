package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;

public record EventAttributeDTO(String name, String value) {
    public static EventAttributeDTO fromTuple(Tuple data) {
        return new EventAttributeDTO(
            data.get("attributeLabel", String.class),
            data.get("attributeValue", String.class)
        );
    }
}
