package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.EventAttr;
import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotNull;

public record AnnotationDTO(
    @NotNull(message = "type cannot be blank")
    String type,

    @NotNull(message = "name cannot be blank")
    String name,

    String value) {

    public AnnotationDTO withValue(String v) {
        return new AnnotationDTO(type, name, v);
    }

    public static AnnotationDTO fromTuple(Tuple data) {
        return new AnnotationDTO(
            data.get("attributeType", String.class),
            data.get("attributeLabel", String.class),
            data.get("attributeValue", String.class)
        );
    }

    public static AnnotationDTO fromEventAttribute(EventAttr attr) {
        return new AnnotationDTO(
            attr.getStudyEventAttr().getGlobalEventAttr().getEventAttrType().getName(),
            attr.getStudyEventAttr().getGlobalEventAttr().getLabel(),
            attr.getValue()
        );
    }
}
