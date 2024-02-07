package edu.ualberta.med.biobank.dtos;

import jakarta.validation.constraints.NotNull;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.util.StringUtil;
import jakarta.persistence.Tuple;

public record AnnotationTypeDTO(

    @NotNull(message = "type cannot be blank")
    String type,

    @NotNull(message = "label cannot be blank")
    String label,

    String status,

    boolean required,

    String[] validValues) {

    public static AnnotationTypeDTO fromTuple(Tuple data) {
        String validValues = data.get("validValues", String.class);
        return new AnnotationTypeDTO(
            data.get("type", String.class),
            data.get("label", String.class),
            Status.fromId(data.get("status", Integer.class)).getName(),
            data.get("required", Boolean.class),
            validValues == null ? null : validValues.split(StringUtil.MUTIPLE_VALUES_DELIMITER)
        );
    }
}
