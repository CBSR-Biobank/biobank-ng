package edu.ualberta.med.biobank.repositories;

import org.springframework.data.jpa.domain.Specification;

import edu.ualberta.med.biobank.domain.Patient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class PatientSpecifications {

    public static Specification<Patient> isPatientNumber(String pnumber) {
        return (Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder builder)
            -> builder.equal(root.get("pnumber"), pnumber);
    }
}
