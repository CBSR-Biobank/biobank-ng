package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;

public record UserDTO(
    Integer userId,
    String fullName,
    String username,
    String password,
    String status,
    HashMap<Integer, GroupDTO> groups,
    HashMap<Integer, MembershipDTO> memberships
) {
    public static UserDTO fromTuple(Tuple data) {
        return new UserDTO(
            data.get("ID", Number.class).intValue(),
            data.get("FULL_NAME", String.class),
            data.get("LOGIN", String.class),
            data.get("PASSWORD", String.class),
            Status.fromId(data.get("ACTIVITY_STATUS_ID", Integer.class)).getName(),
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
        return status.equals(Status.ACTIVE.getName());
    }

    public boolean hasPermission(PermissionEnum permission, Integer centerId, Integer studyId) {
        for (MembershipDTO m : this.memberships.values()) {
            if (m.isAllowed(permission, centerId, studyId)) return true;
        }
        return false;
    }

    public boolean hasAllStudies() {
        for (MembershipDTO m : this.memberships.values()) {
            for (DomainDTO d : m.domains().values()) {
                if (d.allStudies()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Integer> studyIds() {
        Set<Integer> results = new HashSet<>();
        for (MembershipDTO m : this.memberships.values()) {
            for (DomainDTO d : m.domains().values()) {
                results.addAll(d.studyIds());
            }
        }
        return results;
    }

    public boolean hasAllCenters() {
        for (MembershipDTO m : this.memberships.values()) {
            for (DomainDTO d : m.domains().values()) {
                if (d.allCenters()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Integer> centerIds() {
        Set<Integer> results = new HashSet<>();
        for (MembershipDTO m : this.memberships.values()) {
            for (DomainDTO d : m.domains().values()) {
                results.addAll(d.centerIds());
            }
        }
        return results;
    }
}
