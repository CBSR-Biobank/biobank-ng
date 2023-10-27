package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;

public record MembershipDTO(
    Integer id,
    Boolean everyPermission,
    HashMap<Integer, DomainDTO> domains,
    HashMap<Integer, RoleDTO> roles
) {}
