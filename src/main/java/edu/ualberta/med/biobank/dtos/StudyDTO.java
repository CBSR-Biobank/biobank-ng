package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;

public record StudyDTO(Integer id, String name, String nameShort, String status, Integer version) {
    public static StudyDTO fromTuple(Tuple data) {
        return new StudyDTO(
            data.get("id", Number.class).intValue(),
            data.get("name", String.class),
            data.get("nameShort", String.class),
            Status.fromId(data.get("activityStatusId", Integer.class)).toString(),
            data.get("version", Number.class).intValue()
);
    }
}
