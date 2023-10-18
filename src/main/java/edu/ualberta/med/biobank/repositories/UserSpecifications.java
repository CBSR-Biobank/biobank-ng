package edu.ualberta.med.biobank.repositories;

import org.springframework.data.jpa.domain.Specification;

import edu.ualberta.med.biobank.domain.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserSpecifications {

    public static Specification<User> isUsername(String username) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
                -> builder.equal(root.get("login"), username);
    }
}
