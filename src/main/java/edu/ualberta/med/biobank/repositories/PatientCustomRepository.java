package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.dtos.PatientDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class PatientCustomRepository {

    Logger logger = LoggerFactory.getLogger(PatientCustomRepository.class);

    private static final String PATIENT_INFO_HQL =
        """
        SELECT p.id as id,
               p.pnumber as pnumber,
               p.study.id as studyId,
               p.study.nameShort as studyNameShort,
               COUNT(DISTINCT sourceSpecs) as spcCount,
               COUNT(DISTINCT allSpecs) - COUNT(DISTINCT sourceSpecs) as alqCount
        FROM Patient p
        JOIN p.study study
        LEFT JOIN p.collectionEvents ce
        LEFT JOIN ce.originalSpecimens sourceSpecs
        LEFT JOIN ce.allSpecimens allSpecs
        WHERE p.pnumber=?1
        GROUP BY p.id
        """;

    @PersistenceContext
    private EntityManager entityManager;

    private CollectionEventCustomRepository collectionEventCustomRepository;

    PatientCustomRepository(CollectionEventCustomRepository collectionEventCustomRepository) {
        this.collectionEventCustomRepository = collectionEventCustomRepository;
    }

    public Optional<PatientDTO> findByPnumber(String pnumber) {
        try {
            var query = entityManager.createQuery(PATIENT_INFO_HQL, Tuple.class).setParameter(1, pnumber);
            var result = query.getSingleResult();

            var patientId = result.get("id", Number.class).intValue();
            return Optional.of(
                new PatientDTO(
                    patientId,
                    result.get("pnumber", String.class),
                    result.get("spcCount", Number.class).intValue(),
                    result.get("alqCount", Number.class).intValue(),
                    result.get("studyId", Number.class).intValue(),
                    result.get("studyNameShort", String.class),
                    collectionEventCustomRepository.findByPatientId(patientId)
                )
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
