package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(
        attributePaths = {
            "groups",
            "groups.memberships",
            "groups.memberships.domain",
            "groups.memberships.permissions",
            "groups.memberships.roles",
            "groups.memberships.roles.permissions",
            "memberships",
            "memberships.domain",
            "memberships.permissions",
            "memberships.roles",
            "memberships.roles.permissions",
            "csmUser"
        }
    )
    List<User> findByLogin(String login);
}
