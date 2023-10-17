package edu.ualberta.med.biobank.repositories;

import org.springframework.data.jpa.domain.Specification;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Patient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

public class CollectionEventSpecifications {

    public static Specification<CollectionEvent> isVisitNumber(Integer vnumber) {
        return (Root<CollectionEvent> root, CriteriaQuery<?> query, CriteriaBuilder builder)
                -> builder.equal(root.get("visitNumber"), vnumber);
    }

    public static Specification<CollectionEvent> isPatientNumber(String pnumber) {
        return (Root<CollectionEvent> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Join<Patient, CollectionEvent> ceventPatient = root.join("patient");
            return builder.equal(ceventPatient.get("pnumber"), pnumber);
        };
    }
}
