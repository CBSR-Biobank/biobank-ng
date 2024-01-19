package edu.ualberta.med.biobank.domain.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DispatchSpecimenStateConverter implements AttributeConverter<DispatchSpecimenState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DispatchSpecimenState dispatchSpecimenState) {
        if (dispatchSpecimenState == null) {
            return null;
        }
        return dispatchSpecimenState.getId();
    }

    @Override
    public DispatchSpecimenState convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return DispatchSpecimenState.valuesList().stream()
          .filter(c -> c.getId().equals(id))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
