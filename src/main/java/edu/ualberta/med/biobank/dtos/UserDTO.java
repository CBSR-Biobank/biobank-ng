package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;
import edu.ualberta.med.biobank.domain.PermissionEnum;
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
            .filter(g -> g.name() != null && g.name().contains("Global Administrators"))
            .findFirst()
            .isPresent();
    }

    public boolean isActive() {
        return status.equals(Status.ACTIVE);
    }

    public boolean hasPermission(PermissionEnum permission, Integer centerId, Integer studyId) {
        for (MembershipDTO m : this.memberships.values()) {
            if (m.isAllowed(permission, centerId, studyId)) return true;
        }
        return false;
    }
}
