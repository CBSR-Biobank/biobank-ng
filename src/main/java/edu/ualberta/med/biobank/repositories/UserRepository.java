package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.User;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph("user-with-groups")
    List<User> findByLogin(String login);

    // @EntityGraph(
    //     attributePaths = {
    //         "groups",
    //         "groups.memberships",
    //         "groups.memberships.domain",
    //         "groups.memberships.permissions",
    //         "groups.memberships.roles",
    //         "groups.memberships.roles.permissions",
    //         "memberships",
    //         3"memberships.domain",
    //         "memberships.permissions",
    //         "memberships.roles",
    //         "memberships.roles.permissions"
    //     }
    // )
    @EntityGraph("user-with-groups-and-memberships")
    @Query("""
           SELECT u
           FROM User u
           WHERE u.login = (:login)
           """)
    public User findOneWithMemberships(@Param("login") String login);
}
