package edu.ualberta.med.biobank.domain.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LabelingLayoutConverter implements AttributeConverter<LabelingLayout, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LabelingLayout labelingLayout) {
        if (labelingLayout == null) {
            return null;
        }
        return labelingLayout.getId();
    }

    @Override
    public LabelingLayout convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return LabelingLayout.valuesList().stream()
          .filter(c -> c.getId().equals(id))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
