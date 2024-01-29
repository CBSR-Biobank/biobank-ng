package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.EventAttr;
import jakarta.persistence.Tuple;

public record EventAttributeDTO(String name, String value) {
    public static EventAttributeDTO fromTuple(Tuple data) {
        return new EventAttributeDTO(
            data.get("attributeLabel", String.class),
            data.get("attributeValue", String.class)
        );
    }

    public static EventAttributeDTO fromEventAttribute(EventAttr attr) {
        return new EventAttributeDTO(
            attr.getStudyEventAttr().getGlobalEventAttr().getLabel(),
            attr.getValue()
        );
    }
}
