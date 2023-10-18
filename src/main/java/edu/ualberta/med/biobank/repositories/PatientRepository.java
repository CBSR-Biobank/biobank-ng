package edu.ualberta.med.biobank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Patient;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
    @EntityGraph(attributePaths = {"study"})
    Optional<Patient> findById(Integer id);

    @EntityGraph(attributePaths = {"study", "collectionEvents"})
    public List<Patient> findAll(Specification<Patient> spec);
}
