package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.Patient;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @EntityGraph(attributePaths = { "study" })
    Optional<Patient> findById(Integer id);

    @EntityGraph(attributePaths = { "study", "collectionEvents" })
    public List<Patient> findAll(Specification<Patient> spec);

    @Query(
        value = """
                select
                    p.id,
                    p.pnumber,
                    p.CREATED_AT as createdAt,
                    study.id as studyId,
                    study.name_short as studyNameShort,
                    COUNT(distinct ospc.id) as specimenCount,
                    COUNT(distinct aspc.id) - COUNT(distinct ospc.id) as aliquotCount
                from patient p
                join study on study.ID = p.STUDY_ID
                left join collection_event ce on ce.PATIENT_ID = p.ID
                left join specimen ospc on ospc.ORIGINAL_COLLECTION_EVENT_ID = ce.ID
                left join specimen aspc on aspc.COLLECTION_EVENT_ID = ce.ID
                where p.pnumber = :pnumber
                group by p.id;
                """,
        nativeQuery = true
    )
    <T> Collection<T> findByPnumber(String pnumber, Class<T> type);
}
