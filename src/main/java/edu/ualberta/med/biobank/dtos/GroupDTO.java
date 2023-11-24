package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;

public record GroupDTO(Integer groupId, String name) {
    public static GroupDTO fromTuple(Tuple data) {
        return new GroupDTO(
            data.get("GROUP_ID", Number.class).intValue(),
            data.get("GROUP_NAME", String.class)
        );
    }
}
