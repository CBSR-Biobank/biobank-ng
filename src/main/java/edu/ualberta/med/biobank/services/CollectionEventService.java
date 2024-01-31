package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.applicationevents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.dtos.CollectionEventAddDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CommentAddDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.AnnotationDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.Forbidden;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patient.CollectionEventCreatePermission;
import edu.ualberta.med.biobank.permission.patient.CollectionEventDeletePermission;
import edu.ualberta.med.biobank.permission.patient.CollectionEventReadPermission;
import edu.ualberta.med.biobank.permission.patient.CollectionEventUpdatePermission;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private UserService userService;

    private BiobankEventPublisher eventPublisher;

    private Validator validator;

    protected EntityManager em;

    public CollectionEventService(
        CollectionEventRepository collectionEventRepository,
        PatientService patientService,
        UserService userService,
        Validator validator,
        BiobankEventPublisher eventPublisher
    ) {
        this.collectionEventRepository = collectionEventRepository;
        this.patientService = patientService;
        this.userService = userService;
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

    public Either<AppError, CollectionEventDTO> get(String pnumber, Integer vnumber) {
        return getInternal(pnumber, vnumber)
            .flatMap(cevent -> {
                var permission = new CollectionEventReadPermission(cevent.studyId());
                return permission
                    .isAllowed()
                    .flatMap(allowed -> {
                        if (!allowed) {
                            return Either.left(new PermissionError("study"));
                        }

                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        eventPublisher.publishVisitRead(auth.getName(), cevent.patientNumber(), cevent.visitNumber());
                        return Either.right(cevent);
                    });
            });
    }

    public Either<AppError, CollectionEventDTO> add(String pnumber, CollectionEventAddDTO ceventInfo) {
        if (pnumber == null) {
            throw new IllegalArgumentException("patient number cannot be null");
        }
        if (ceventInfo == null) {
            throw new IllegalArgumentException("ceventInfo cannot be null");
        }

        Set<ConstraintViolation<CollectionEventAddDTO>> violations = validator.validate(ceventInfo);
        if (!violations.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<CollectionEventAddDTO> constraintViolation : violations) {
                errors.add(constraintViolation.getMessage());
            }
            return Either.left(new ValidationError(String.join(", ", errors)));
        }

        return patientService
            .getInternal(pnumber)
            .flatMap(patientDTO -> {
                CollectionEventCreatePermission permission = new CollectionEventCreatePermission(patientDTO.studyId());
                return permission
                    .isAllowed()
                    .flatMap(allowed -> {
                        if (!allowed) {
                            return Either.left(new PermissionError("collection event add"));
                        }
                        return Either.right(allowed);
                    })
                    .flatMap(ignored -> patientService.getByPatientId(patientDTO.id()))
                    .flatMap(patient -> {
                        List<Integer> vnumbers = patientDTO
                            .collectionEvents()
                            .stream()
                            .map(ce -> ce.visitNumber())
                            .toList();

                        if (vnumbers.contains(ceventInfo.vnumber())) {
                            return Either.left(new ValidationError("visit already exists"));
                        }

                        var newEvent = new CollectionEvent();
                        newEvent.setPatient(patient);
                        newEvent.setVisitNumber(ceventInfo.vnumber());
                        newEvent.setActivityStatus(Status.ACTIVE);
                        collectionEventRepository.save(newEvent);

                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        eventPublisher.publishVisitCreated(auth.getName(), pnumber, ceventInfo.vnumber());

                        return Either.right(CollectionEventDTO.fromCollectionEvent(newEvent));
                    });
            });
    }

    public Either<AppError, Boolean> delete(String pnumber, Integer vnumber) {
        return getInternal(pnumber, vnumber)
            .flatMap(ceventDTO -> {
                CollectionEventDeletePermission permission = new CollectionEventDeletePermission(ceventDTO.studyId());
                return permission
                    .isAllowed()
                    .flatMap(allowed -> {
                        if (!allowed) {
                            return Either.left(new PermissionError("collection event delete"));
                        }

                        if (!ceventDTO.sourceSpecimens().isEmpty()) {
                            return Either.left(new Forbidden("collection event has specimens"));
                        }

                        collectionEventRepository.deleteById(ceventDTO.id());

                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        eventPublisher.publishVisitDeleted(auth.getName(), pnumber, vnumber);

                        return Either.right(true);
                    });
            });
    }

    public Either<AppError, Collection<CommentDTO>> getComments(String pnumber, Integer vnumber) {
        return getInternal(pnumber, vnumber)
            .flatMap(cevent -> {
                var permission = new CollectionEventReadPermission(cevent.studyId());
                return permission
                    .isAllowed()
                    .flatMap(allowed -> {
                        if (!allowed) {
                            return Either.left(new PermissionError("collection event read"));
                        }

                        Map<Integer, CommentDTO> comments = new LinkedHashMap<>();

                        collectionEventRepository
                            .comments(pnumber, vnumber, Tuple.class)
                            .stream()
                            .forEach(row -> {
                                var commentId = row.get("commentId", Integer.class);
                                if (commentId != null) {
                                    comments.computeIfAbsent(commentId, id -> CommentDTO.fromTuple(row));
                                }
                            });

                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        eventPublisher.publishVisitRead(auth.getName(), cevent.patientNumber(), cevent.visitNumber());
                        return Either.right(comments.values());
                    });
            });
    }

    public Either<AppError, CommentDTO> addComment(String pnumber, Integer vnumber, CommentAddDTO commentDTO) {
        var violations = validator.validate(commentDTO);

        if (!violations.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<CommentAddDTO> violation : violations) {
                errors.add(violation.getMessage());
            }
            return Either.left(new ValidationError(String.join(", ", errors)));
        }

        return getInternal(pnumber, vnumber)
            .flatMap(ceventDTO -> {
                var permission = new CollectionEventUpdatePermission(ceventDTO.studyId());
                return permission
                    .isAllowed()
                    .flatMap(allowed -> {
                        if (!allowed) {
                            return Either.left(new PermissionError("study"));
                        }
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        return userService.findOneWithMemberships(auth.getName());
                    })
                    .flatMap(userDto -> {
                        User user = userService.getById(userDto.userId());

                        Comment comment = new Comment();
                        comment.setUser(user);
                        comment.setCreatedAt(new Date());
                        comment.setMessage(commentDTO.message().trim());

                        CollectionEvent eventToUpdate = collectionEventRepository.getReferenceById(ceventDTO.id());
                        eventToUpdate.getComments().add(comment);
                        collectionEventRepository.save(eventToUpdate);

                        eventPublisher.publishVisitUpdated(userDto.username(), pnumber, vnumber);
                        return Either.right(CommentDTO.fromComment(comment));
                    });
            });
    }

    /**
     * This method is only visible to the package. It is not meant to be used outside the package.
     *
     * It does not check for user permissions or update the LOG table.
     */
    Either<AppError, CollectionEventDTO> getInternal(String pnumber, Integer vnumber) {
        Map<Integer, CollectionEventDTO> cevents = new HashMap<>();
        Map<Integer, AnnotationDTO> attributes = new HashMap<>();
        Map<Integer, SourceSpecimenDTO> sourceSpecimens = new HashMap<>();

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
                    attributes.computeIfAbsent(attributeId, id -> AnnotationDTO.fromTuple(row));
                }

                var isSourceSpecimen = row.get("isSourceSpecimen", Integer.class);
                if (isSourceSpecimen == 1) {
                    var specimenId = row.get("specimenId", Integer.class);
                    if (specimenId != null) {
                        sourceSpecimens.computeIfAbsent(specimenId, id -> SourceSpecimenDTO.fromTuple(row));
                    }
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
                new ArrayList<>(sourceSpecimens.values())
            );

        return Either.right(cevent);
    }
}
