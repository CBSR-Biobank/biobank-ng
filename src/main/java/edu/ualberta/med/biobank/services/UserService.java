package edu.ualberta.med.biobank.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.User;
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

    public User getById(int id) {
        return userRepository.getReferenceById(id);
    }

    public Either<AppError, UserDTO> findByLogin(String login) {
        var users = userTuplesToDTO(userRepository.findByLogin(login, Tuple.class));
        if (users.size() <= 0) {
            return Either.left(new EntityNotFound("user by login"));
        }

        var user = users.stream().findFirst().get();
        logger.debug("user: {}", LoggingUtils.prettyPrintJson(user));
        return Either.right(user);
    }

    public Either<AppError, UserDTO> findByApiKey(String apiKey) {
        var users = userTuplesToDTO(userRepository.findByApiKey(apiKey, Tuple.class));
        if (users.size() <= 0) {
            return Either.left(new EntityNotFound("user by API key"));
        }

        var user = users.stream().findFirst().get();
        logger.info(">>> users: %s".formatted(LoggingUtils.prettyPrintJson(user)));
        logger.debug("user: {}", LoggingUtils.prettyPrintJson(user));
        return Either.right(user);
    }

    public Either<AppError, UserDTO> findOneWithMemberships(String username) {
        var users = userTuplesToDTO(userRepository.findByLogin(username, Tuple.class));
        if (users.size() != 1) {
            return Either.left(new EntityNotFound("user by username"));
        }
        var user = users.stream().findFirst().get();
        return Either.right(user);
    }

    private Collection<UserDTO> userTuplesToDTO(Collection<Tuple> tuples) {
        Map<Integer, UserDTO> users = new HashMap<>();

        tuples
            .stream()
            .forEach(row -> {
                var userId = row.get("ID", Integer.class);
                users.computeIfAbsent(userId, id -> UserDTO.fromTuple(row));

                var groupId = row.get("GROUP_ID", Integer.class);
                if (groupId != null) {
                    users.get(userId).groups().computeIfAbsent(groupId, id -> GroupDTO.fromTuple(row));
                }

                var membershipId = row.get("MEMBERSHIP_ID", Integer.class);
                if (membershipId == null) {
                    throw new RuntimeException("membership is null");
                }
                users.get(userId).memberships().computeIfAbsent(membershipId, id -> MembershipDTO.fromTuple(row));

                var membership = users.get(userId).memberships().get(membershipId);
                var domainId = row.get("DOMAIN_ID", Integer.class);
                if (domainId != null) {
                    membership.domains().computeIfAbsent(domainId, id -> DomainDTO.fromTuple(row));

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
                    membership.roles().computeIfAbsent(roleId, id -> RoleDTO.fromTuple(row));

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
