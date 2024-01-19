package edu.ualberta.med.biobank.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getId();
    }

    @Override
    public Status convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return Status.valuesList().stream()
          .filter(c -> c.getId().equals(id))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
