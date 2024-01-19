package edu.ualberta.med.biobank.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PermissionEnumConverter implements AttributeConverter<PermissionEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PermissionEnum permissionEnum) {
        if (permissionEnum == null) {
            return null;
        }
        return permissionEnum.getId();
    }

    @Override
    public PermissionEnum convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return PermissionEnum.valuesList().stream()
          .filter(c -> c.getId().equals(id))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
