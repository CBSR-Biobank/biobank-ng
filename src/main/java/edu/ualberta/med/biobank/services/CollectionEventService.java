package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.applicationevents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.CollectionEventAddDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.EventAttributeDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patient.CollectionEventReadPermission;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CollectionEventService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(CollectionEventService.class);

    private CollectionEventRepository collectionEventRepository;

    private PatientService patientService;

    private BiobankEventPublisher eventPublisher;

    private Validator validator;

    protected EntityManager em;

    public CollectionEventService(
        CollectionEventRepository collectionEventRepository,
        PatientService patientService,
        Validator validator,
        BiobankEventPublisher eventPublisher
    ) {
        this.collectionEventRepository = collectionEventRepository;
        this.patientService = patientService;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
    }

    public Either<AppError, CollectionEvent> getByCollectionEventId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

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

        if (pnumber == null) {
            throw new IllegalArgumentException("patient number cannot be null");
        }
        if (vnumber == null) {
            throw new IllegalArgumentException("visit number cannot be null");
        }

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

        final var cevent = cevents
            .entrySet()
            .iterator()
            .next()
            .getValue()
            .withExtras(
                new ArrayList<>(attributes.values()),
                new ArrayList<>(comments.values()),
                new ArrayList<>(sourceSpecimens.values())
            );
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

    public Either<AppError, CollectionEventDTO> addCollectionEvent(String pnumber, CollectionEventAddDTO ceventInfo) {
        if (pnumber == null) {
            throw new IllegalArgumentException("patient number cannot be null");
        }
        if (ceventInfo == null) {
            throw new IllegalArgumentException("ceventInfo cannot be null");
        }

        var violations = validator.validate(ceventInfo);

        if (!violations.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<CollectionEventAddDTO> constraintViolation : violations) {
                errors.add(constraintViolation.getMessage());
            }
            return Either.left(new ValidationError(String.join(", ", errors)));
        }

        // TODO: check for collection event create permission

        return patientService
            .findByPnumber(pnumber)
            .flatMap(patientDTO -> {
                var found = findByPnumberAndVnumber(pnumber, ceventInfo.vnumber());
                if (found.isRight()) {
                    return Either.left(new ValidationError("visit already exists"));
                }

                return patientService
                    .getByPatientId(patientDTO.id())
                    .map(patient -> {
                        var newEvent = new CollectionEvent();
                        newEvent.setPatient(patient);
                        newEvent.setVisitNumber(ceventInfo.vnumber());
                        newEvent.setActivityStatus(Status.ACTIVE);
                        collectionEventRepository.save(newEvent);

                        logger.info(">>> {}", LoggingUtils.prettyPrintJson(ceventInfo));

                        return CollectionEventDTO.fromCollectionEvent(newEvent);
                    });
            });
    }
}
