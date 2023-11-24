package edu.ualberta.med.biobank.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.DomainDTO;
import edu.ualberta.med.biobank.dtos.GroupDTO;
import edu.ualberta.med.biobank.dtos.MembershipDTO;
import edu.ualberta.med.biobank.dtos.RoleDTO;
import edu.ualberta.med.biobank.dtos.UserDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.repositories.UserRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @PersistenceContext
    private EntityManager em;

    private UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Either<AppError, UserDTO> findByLogin(String login) {
        var users = userByLoginToDTO(login);
        if (users.size() <= 0) {
            return Either.left(new EntityNotFound("user by login"));
        }

        var user = users.stream().findFirst().get();
        logger.info("user: {}", LoggingUtils.prettyPrintJson(user));
        return Either.right(user);
    }

    public Either<AppError, UserDTO> findOneWithMemberships(String username) {
        var users = userByLoginToDTO(username);
        if (users.size() != 1) {
            return Either.left(new EntityNotFound("user by username"));
        }
        var user = users.stream().findFirst().get();
        return Either.right(user);
    }

    private Collection<UserDTO> userByLoginToDTO(String login) {
        Map<Integer, UserDTO> users = new HashMap<>();

        userRepository
            .findByLogin(login, Tuple.class)
            .stream()
            .forEach(row -> {
                var userId = row.get("ID", Integer.class);
                users.computeIfAbsent(
                    userId,
                    id ->
                        new UserDTO(
                            id,
                            row.get("LOGIN", String.class),
                            row.get("PASSWORD", String.class),
                            Status.fromId(row.get("ACTIVITY_STATUS_ID", Integer.class)),
                            new HashMap<>(),
                            new HashMap<>()
                        )
                );

                var groupId = row.get("GROUP_ID", Integer.class);
                if (groupId != null) {
                    users
                        .get(userId)
                        .groups()
                        .computeIfAbsent(groupId, id -> new GroupDTO(groupId, row.get("GROUP_NAME", String.class)));
                }

                var membershipId = row.get("MEMBERSHIP_ID", Integer.class);
                if (membershipId == null) {
                    throw new RuntimeException("membership is null");
                }
                users
                    .get(userId)
                    .memberships()
                    .computeIfAbsent(
                        membershipId,
                        id ->
                            new MembershipDTO(
                                membershipId,
                                row.get("EVERY_PERMISSION", Boolean.class),
                                new HashMap<>(),
                                new HashMap<>(),
                                new HashSet<>()
                            )
                    );

                var membership = users.get(userId).memberships().get(membershipId);
                var domainId = row.get("DOMAIN_ID", Integer.class);
                if (domainId != null) {
                    membership
                        .domains()
                        .computeIfAbsent(
                            domainId,
                            id ->
                                new DomainDTO(
                                    domainId,
                                    row.get("ALL_CENTERS", Boolean.class),
                                    row.get("ALL_STUDIES", Boolean.class),
                                    new HashSet<>(),
                                    new HashSet<>()
                                )
                        );

                    var domain = membership.domains().get(domainId);
                    var centerId = row.get("CENTER_ID", Integer.class);
                    if (centerId != null) {
                        domain.centerIds().add(centerId);
                    }

                    var studyId = row.get("STUDY_ID", Integer.class);
                    if (studyId != null) {
                        domain.studyIds().add(studyId);
                    }
                }

                var roleId = row.get("ROLE_ID", Integer.class);
                if (roleId != null) {
                    membership
                        .roles()
                        .computeIfAbsent(
                            roleId,
                            id -> new RoleDTO(roleId, row.get("ROLE_NAME", String.class), new HashSet<>())
                        );

                    var role = membership.roles().get(roleId);
                    var permissionId = row.get("ROLE_PERMISSION_ID", Integer.class);
                    if (permissionId != null) {
                        role.permissions().add(PermissionEnum.fromId(permissionId));
                    }
                }

                var membershipPermissionId = row.get("MEMBERSHIP_PERMISSION_ID", Integer.class);
                if (membershipPermissionId != null) {
                    membership.permissions().add(PermissionEnum.fromId(membershipPermissionId));
                }

            });

        return users.values();
    }
}
