package edu.ualberta.med.biobank.repositories;

import java.util.List;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import jakarta.persistence.EntityManager;
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
        SELECT p,
               COUNT(DISTINCT sourceSpecs) as spcCount,
               COUNT(DISTINCT allSpecs) - COUNT(DISTINCT sourceSpecs) as alqCount
        FROM Patient p
        JOIN FETCH p.study study
        LEFT JOIN FETCH study.researchGroup rgroup
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
        var query = entityManager.createQuery(PATIENT_INFO_HQL, Tuple.class).setParameter(1, pnumber);
        var results = query.getResultList();
        var result = results.get(0);

        var patient = (Patient) result.get(0);
        return Optional.of(
            toPatientDTO(
                patient,
                ((Number) result.get("spcCount")).intValue(),
                ((Number) result.get("alqCount")).intValue(),
                collectionEventCustomRepository.findByPatientId(patient.getId())
            )
        );
    }

    public static PatientDTO toPatientDTO(
        Patient patient,
        Integer sourceCount,
        Integer aliquotCount,
        List<CollectionEventSummaryDTO> collectionEventsDTOs
    ) {
        return new PatientDTO(
            patient.getPnumber(),
            sourceCount,
            aliquotCount,
            patient.getStudy().getId(),
            patient.getStudy().getNameShort(),
            collectionEventsDTOs
        );
    }
}
