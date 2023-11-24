package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;

public record UserDTO(
    Integer userId,
    String username,
    String password,
    Status status,
    HashMap<Integer, GroupDTO> groups,
    HashMap<Integer, MembershipDTO> memberships
) {
    public static UserDTO fromTuple(Tuple data) {
        return new UserDTO(
            data.get("ID", Number.class).intValue(),
            data.get("LOGIN", String.class),
            data.get("PASSWORD", String.class),
            Status.fromId(data.get("ACTIVITY_STATUS_ID", Integer.class)),
            new HashMap<>(),
            new HashMap<>()
        );
    }

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
