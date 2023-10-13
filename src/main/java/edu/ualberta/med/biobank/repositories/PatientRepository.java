package edu.ualberta.med.biobank.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
    @Query(
           value = """
           SELECT p
           FROM Patient p
           JOIN FETCH p.study study
           LEFT JOIN FETCH study.researchGroup rgroup
           LEFT JOIN FETCH p.collectionEvents ce
           LEFT JOIN FETCH ce.eventAttrs evattr
           LEFT JOIN FETCH evattr.studyEventAttr sattr
           LEFT JOIN FETCH sattr.globalEventAttr gattr
           WHERE p.pnumber = :pnumber
           """
           )
    List<Patient> findByPnumber(String pnumber);
}
