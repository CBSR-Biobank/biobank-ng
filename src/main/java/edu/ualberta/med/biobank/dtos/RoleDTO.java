package edu.ualberta.med.biobank.dtos;

import java.util.Set;

import edu.ualberta.med.biobank.domain.PermissionEnum;

public record RoleDTO(
    Integer id,
    String roleName,
    Set<PermissionEnum> permissions
) {}
