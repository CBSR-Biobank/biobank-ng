package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.EventAttributeDTO;
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

    Logger logger = LoggerFactory.getLogger(CollectionEventCustomRepository.class);

    // LEFT JOIN FETCH cevent.eventAttrs eventAttrs
    // LEFT JOIN FETCH eventAttrs.studyEventAttr studyEventAttr
    // LEFT JOIN FETCH studyEventAttr.globalEventAttr globalEventAttr

    private static final String CEVENT_INFO_QRY =
        """
        SELECT DISTINCT cevent
        FROM CollectionEvent as cevent
        LEFT JOIN FETCH cevent.comments comments
        LEFT JOIN FETCH comments.user
        WHERE cevent.patient.id=?1
        """;

    private static final String CEVENT_COUNT_INFO_QRY =
        """
        SELECT cevent.id as id,
               COUNT(DISTINCT sourcesSpecs) as numSourceSpecimens,
               COUNT(DISTINCT allSpecs) - COUNT(DISTINCT sourcesSpecs) as numAliquots,
               MIN(sourcesSpecs.createdAt) as createdAt
        FROM CollectionEvent as cevent
        LEFT JOIN cevent.originalSpecimens as sourcesSpecs
        LEFT JOIN cevent.allSpecimens as allSpecs
        WHERE cevent.patient.id=?1
        GROUP BY cevent.id
        """;

    @PersistenceContext
    private EntityManager entityManager;

    public List<CollectionEventSummaryDTO> findByPatientId(Integer patientId) {
        var counts = collectionEventSpecimenCounts(patientId);
        var query = entityManager.createQuery(CEVENT_INFO_QRY).setParameter(1, patientId);
        return query
            .getResultStream()
            .map(obj -> {
                var cevent = (CollectionEvent) obj;
                return toCollectionEventDTO(cevent, counts.get(cevent.getId()));
            })
            .toList();
    }

    public static CollectionEventSummaryDTO toCollectionEventDTO(CollectionEvent cevent, List<Integer> counts) {
        return new CollectionEventSummaryDTO(
            cevent.getId(),
            cevent.getVisitNumber(),
            counts.get(0),
            counts.get(1),
            cevent.getActivityStatus().getName(),
            cevent
                .getComments()
                .stream()
                .map(comment ->
                    new CommentDTO(
                        comment.getId(),
                        comment.getMessage(),
                        comment.getUser().getFullName(),
                        comment.getCreatedAt().toString()
                    )
                )
                .toList()
        );
    }

    private Map<Integer, List<Integer>> collectionEventSpecimenCounts(Integer patientId) {
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
