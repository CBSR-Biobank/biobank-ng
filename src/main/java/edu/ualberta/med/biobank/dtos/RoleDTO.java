package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.PermissionEnum;
import jakarta.persistence.Tuple;
import java.util.HashSet;
import java.util.Set;

public record RoleDTO(Integer id, String roleName, Set<PermissionEnum> permissions) {
    public static RoleDTO fromTuple(Tuple data) {
        return new RoleDTO(
            data.get("ROLE_ID", Number.class).intValue(),
            data.get("ROLE_NAME", String.class),
            new HashSet<>()
        );
    }
}
