package edu.ualberta.med.biobank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationevents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.EventAttributeDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.permission.patient.CollectionEventReadPermission;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CollectionEventService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(CollectionEventService.class);

    @Autowired
    CollectionEventRepository collectionEventRepository;

    private BiobankEventPublisher eventPublisher;

    public CollectionEventService(
        CollectionEventRepository collectionEventRepository,
        BiobankEventPublisher eventPublisher
    ) {
        this.collectionEventRepository = collectionEventRepository;
        this.eventPublisher = eventPublisher;
    }

    public Either<AppError, CollectionEvent> getByCollectionEventId(Integer id) {
        return collectionEventRepository
            .findById(id)
            .map(Either::<AppError, CollectionEvent>right)
            .orElseGet(() -> Either.left(new EntityNotFound("collectionEvent")));
    }

    public Either<AppError, CollectionEventDTO> findByPnumberAndVnumber(String pnumber, Integer vnumber) {
        Map<Integer, CollectionEventDTO> cevents = new HashMap<>();
        Map<Integer, EventAttributeDTO> attributes = new HashMap<>();
        Map<Integer, SourceSpecimenDTO> sourceSpecimens = new HashMap<>();
        Map<Integer, CommentDTO> comments = new HashMap<>();

        collectionEventRepository
            .findByPatientAndVnumber(pnumber, vnumber, Tuple.class)
            .stream()
            .forEach(row -> {
                var ceventId = row.get("id", Integer.class);
                cevents.computeIfAbsent(ceventId, id -> CollectionEventDTO.fromTuple(row));

                var attributeId = row.get("attributeId", Integer.class);
                if (attributeId != null) {
                    attributes.computeIfAbsent(attributeId, id -> EventAttributeDTO.fromTuple(row));
                }

                var isSourceSpecimen = row.get("isSourceSpecimen", Integer.class);
                if (isSourceSpecimen == 1) {
                    var specimenId = row.get("specimenId", Integer.class);
                    if (specimenId != null) {
                        sourceSpecimens.computeIfAbsent(specimenId, id -> SourceSpecimenDTO.fromTuple(row));
                    }
                }

                var commentId = row.get("commentId", Integer.class);
                if (commentId != null) {
                    comments.computeIfAbsent(commentId, id -> CommentDTO.fromTuple(row));
                }

            });

        if (cevents.size() != 1) {
            return Either.left(new EntityNotFound("collection event by pnumber and vnumber"));
        }

        final var cevent = cevents.entrySet().iterator().next().getValue()
            .withExtras(
                new ArrayList<>(attributes.values()),
                new ArrayList<>(comments.values()),
                new ArrayList<>(sourceSpecimens.values()));
        var permission = new CollectionEventReadPermission(cevent.studyId());
        var allowedMaybe = permission.isAllowed();
        return allowedMaybe
            .flatMap(allowed -> {
                if (!allowed) {
                    return Either.left(new PermissionError("study"));
                }
                return Either.right(cevent);
            })
            .map(ce -> {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                eventPublisher.publishVisitRead(auth.getName(), cevent.patientNumber(), cevent.visitNumber());
                return ce;
            });
    }
}
