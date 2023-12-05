package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import edu.ualberta.med.biobank.domain.Membership;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.PermissionEnum.Require;
import jakarta.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record MembershipDTO(
    Integer id,
    Boolean everyPermission,
    HashMap<Integer, DomainDTO> domains,
    HashMap<Integer, RoleDTO> roles,
    Set<PermissionEnum> permissions
) {

    private static final Logger logger = LoggerFactory.getLogger(MembershipDTO.class);

    public static MembershipDTO fromTuple(Tuple data) {
        return new MembershipDTO(
            data.get("MEMBERSHIP_ID", Number.class).intValue(),
            data.get("EVERY_PERMISSION", Boolean.class),
            new HashMap<>(),
            new HashMap<>(),
            new HashSet<>()
        );
    }

    /**
     * This is a confusing check. If centerId is null, it means we do not care about its
     * value, otherwise, {@link DomainDTO#hasCenter(centerId)} must be true. The same applies to
     * studyId.
     */
    public boolean isAllowed(PermissionEnum permission, Integer centerId, Integer studyId) {
        boolean requiresMet = isRequirementsMet(permission);
        var domain = getDomain();
        boolean hasCenter = centerId == null || domain.hasCenter(centerId);
        boolean hasStudy = studyId == null || domain.hasStudy(studyId);
        boolean hasPermission = hasPermission(permission);
        boolean allowed = requiresMet && hasCenter && hasStudy && hasPermission;
        return allowed;
    }

    /**
     * Return true if special requirements (i.e. {@link Require}-s) are met on the given
     * {@link Membership}, otherwise false.
     */
    public boolean isRequirementsMet(PermissionEnum permission) {
        if (domains.size() != 1) {
            throw new RuntimeException("membership has to have a single domain");
        }

        boolean reqsMet = true;
        DomainDTO d = getDomain();
        var requires = permission.getRequires();
        reqsMet &= !requires.contains(Require.ALL_CENTERS) || d.allCenters();
        reqsMet &= !requires.contains(Require.ALL_STUDIES) || d.allStudies();
        return reqsMet;
    }

    public boolean hasPermission(PermissionEnum permission) {
        if (everyPermission) {
            return true;
        }
        return getAllPermissions().contains(permission);
    }

    public Set<PermissionEnum> getAllPermissions() {
        Set<PermissionEnum> allPermissions = new HashSet<PermissionEnum>();
        if (everyPermission) {
            allPermissions.addAll(PermissionEnum.valuesList());
        } else {
            permissions.addAll(permissions);
            for (RoleDTO role : roles.values()) {
                permissions.addAll(role.permissions());
            }
        }
        return permissions;
    }

    private DomainDTO getDomain() {
        if (domains.size() != 1) {
            throw new RuntimeException("membership has to have a single domain");
        }

        return domains
            .values()
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("membership does not have a domain"));
    }
}
