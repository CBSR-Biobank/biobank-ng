package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;

import edu.ualberta.med.biobank.domain.Status;

public record UserDTO(
    Integer userId,
    String username,
    String password,
    Status status,
    HashMap<Integer, GroupDTO> groups,
    HashMap<Integer, MembershipDTO> memberships
) {

    public boolean isGlobalAdmin() {
        return groups.values().stream()
            .filter(g -> g.name().contains("Global Administrators"))
            .findFirst()
            .isPresent();
    }

    public boolean isActive() {
        return status.equals(Status.ACTIVE);
    }

}
