package edu.ualberta.med.biobank.repositories;

import org.springframework.data.jpa.domain.Specification;

import edu.ualberta.med.biobank.domain.Patient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class SpecimenSpecifications {

    public static Specification<Patient> isInventoryId(String inventoryId) {
        return (Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder builder)
            -> builder.equal(root.get("inventoryId"), inventoryId);
    }
}
