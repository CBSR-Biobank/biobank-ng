package edu.ualberta.med.biobank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Specimen;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface SpecimenRepository extends JpaRepository<Specimen, Integer>{
    @EntityGraph(attributePaths = {"study"})
    Optional<Specimen> findById(Integer id);

    @EntityGraph(attributePaths = {"study", "collectionEvents"})
    public List<Specimen> findAll(Specification<Specimen> spec);
}
