package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.dtos.CollectionEventInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionEventCustomRepository {

    final Logger logger = LoggerFactory.getLogger(CollectionEventCustomRepository.class);

    private static final String CEVENT_COUNTS_BY_PATIENT_ID_QRY =
        """
        SELECT cevent.id as id,
               COUNT(DISTINCT sourceSpecimens) as numSourceSpecimens,
               COUNT(DISTINCT allSpecimens) - COUNT(DISTINCT sourceSpecimens) as numAliquots,
               MIN(sourceSpecimens.createdAt) as createdAt
        FROM CollectionEvent as cevent
        LEFT JOIN cevent.originalSpecimens as sourceSpecimens
        LEFT JOIN cevent.allSpecimens as allSpecimens
        WHERE cevent.patient.id=?1
        GROUP BY cevent.id
        """;

    @PersistenceContext
    private EntityManager entityManager;

    // returns a map of the collection event counts for a patient
    //
    // the keys of the map are the collection event IDs
    public Map<Integer, CollectionEventInfoDTO> collectionEventCountsByPatientId(Integer patientId) {
        var result = new HashMap<Integer, CollectionEventInfoDTO>();
        var query = entityManager.createQuery(CEVENT_COUNTS_BY_PATIENT_ID_QRY, Tuple.class).setParameter(1, patientId);

        var rows = query.getResultList();
        for (var row : rows) {
            result.computeIfAbsent(
                row.get("id", Integer.class),
                id ->
                    new CollectionEventInfoDTO(
                        id,
                        row.get("numSourceSpecimens", Number.class).intValue(),
                        row.get("numAliquots", Number.class).intValue(),
                        row.get("createdAt", Date.class)
                    )
            );
        }
        return result;
    }
}
