package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.User;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph("user-with-groups")
    List<User> findByLogin(String login);

    @EntityGraph("user-with-groups-and-memberships")
    public List<User> findAll(Specification<User> spec);
}
