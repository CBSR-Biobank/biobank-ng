package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
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
        SELECT cevent.id,
               COUNT(DISTINCT sourcesSpecs),
               COUNT(DISTINCT allSpecs) - COUNT(DISTINCT sourcesSpecs),
               MIN(sourcesSpecs.createdAt)
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
        var query = entityManager.createQuery(CEVENT_COUNT_INFO_QRY, Tuple.class).setParameter(1, patientId);
        var result = new HashMap<Integer, List<Integer>>();

        var rows = query.getResultList();
        for (var row : rows) {
            var ceventId = ((Number) row.get(0)).intValue();
            var specimenCount = ((Number) row.get(1)).intValue();
            var aliquotCount = ((Number) row.get(2)).intValue();
            result.put(ceventId, List.of(specimenCount, aliquotCount));
        }
        return result;
    }
}
