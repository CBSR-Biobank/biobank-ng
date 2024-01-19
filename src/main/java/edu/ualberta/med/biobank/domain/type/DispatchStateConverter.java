package edu.ualberta.med.biobank.domain.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DispatchStateConverter implements AttributeConverter<DispatchState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DispatchState dispatchState) {
        if (dispatchState == null) {
            return null;
        }
        return dispatchState.getId();
    }

    @Override
    public DispatchState convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return DispatchState.valuesList().stream()
          .filter(c -> c.getId().equals(id))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
