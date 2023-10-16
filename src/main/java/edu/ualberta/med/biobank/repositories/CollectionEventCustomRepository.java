package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionEventCustomRepository {

    final Logger logger = LoggerFactory.getLogger(CollectionEventCustomRepository.class);

    private static final String CEVENT_COUNT_INFO_QRY =
        """
        SELECT cevent.id as id,
               COUNT(DISTINCT sourcesSpecimens) as numSourceSpecimens,
               COUNT(DISTINCT allSpecimens) - COUNT(DISTINCT sourcesSpecimens) as numAliquots,
               MIN(sourcesSpecimens.createdAt) as createdAt
        FROM CollectionEvent as cevent
        LEFT JOIN cevent.originalSpecimens as sourcesSpecimens
        LEFT JOIN cevent.allSpecimens as allSpecimens
        WHERE cevent.patient.id=?1
        GROUP BY cevent.id
        """;

    @PersistenceContext
    private EntityManager entityManager;

    // returns a map of the collection event counts for a patient
    //
    // the keys of the map are the collection event IDs
    public Map<Integer, List<Integer>> collectionEventSpecimenCounts(Integer patientId) {
        var result = new HashMap<Integer, List<Integer>>();
        var query = entityManager.createQuery(CEVENT_COUNT_INFO_QRY, Tuple.class).setParameter(1, patientId);

        var rows = query.getResultList();
        for (var row : rows) {
            result.computeIfAbsent(
                row.get("id", Integer.class),
                id ->
                    List.of(
                        row.get("numSourceSpecimens", Number.class).intValue(),
                        row.get("numAliquots", Number.class).intValue()
                    )
            );
        }
        return result;
    }
}
