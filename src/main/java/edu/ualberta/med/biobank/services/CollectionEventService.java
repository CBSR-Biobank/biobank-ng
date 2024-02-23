package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.applicationevents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.EventAttr;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.StudyEventAttr;
import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.dtos.AnnotationDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventAddDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventUpdateDTO;
import edu.ualberta.med.biobank.dtos.CommentAddDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.Forbidden;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patients.CollectionEventCreatePermission;
import edu.ualberta.med.biobank.permission.patients.CollectionEventDeletePermission;
import edu.ualberta.med.biobank.permission.patients.CollectionEventReadPermission;
import edu.ualberta.med.biobank.permission.patients.CollectionEventUpdatePermission;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.repositories.EventAttrRepository;
import edu.ualberta.med.biobank.util.DateUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import edu.ualberta.med.biobank.util.StringUtil;
import io.jbock.util.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CollectionEventService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(CollectionEventService.class);

    private class EventAttrValuePair {

        public StudyEventAttr studyAttr;
        public String value;
    }

    private CollectionEventRepository collectionEventRepository;

    private EventAttrRepository eventAttrRepository;

    private PatientService patientService;

    private UserService userService;

    private BiobankEventPublisher eventPublisher;

    private Validator validator;

    @PersistenceContext
    protected EntityManager em;

    public CollectionEventService(
        CollectionEventRepository collectionEventRepository,
        EventAttrRepository eventAttrRepository,
        PatientService patientService,
        UserService userService,
        Validator validator,
        BiobankEventPublisher eventPublisher
    ) {
        this.collectionEventRepository = collectionEventRepository;
        this.eventAttrRepository = eventAttrRepository;
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
                        eventPublisher.publishVisitRead(auth.getName(), cevent.pnumber(), cevent.vnumber());
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

    @Transactional
    public Either<AppError, CollectionEventDTO> update(
        String pnumber,
        Integer vnumber,
        CollectionEventUpdateDTO ceventInfo
    ) {
        var violations = validator.validate(ceventInfo);

        if (!violations.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<CollectionEventUpdateDTO> constraintViolation : violations) {
                errors.add(constraintViolation.getMessage());
            }
            return Either.left(new ValidationError(String.join(", ", errors)));
        }

        return getInternal(pnumber, vnumber)
            .flatMap(ceventDTO -> {
                var permission = new CollectionEventUpdatePermission(ceventDTO.studyId());
                return permission
                    .isAllowed()
                    .flatMap(allowed -> {
                        logger.debug("ceventInfo: {}", LoggingUtils.prettyPrintJson(ceventInfo));

                        if (!allowed) {
                            return Either.left(new PermissionError("study: %s".formatted(ceventDTO.studyNameShort())));
                        }

                        if (vnumber != ceventInfo.vnumber() && getInternal(pnumber, ceventInfo.vnumber()).isRight()) {
                            return Either.left(
                                new Forbidden("visit number exists: %d".formatted(ceventInfo.vnumber()))
                            );
                        }

                        Status newStatus = Status.fromName(ceventInfo.status());
                        if (newStatus == null) {
                            return Either.left(
                                new ValidationError("invalid status: %s".formatted(ceventInfo.status()))
                            );
                        }

                        CollectionEvent cevent = collectionEventRepository.getReferenceById(ceventDTO.id());
                        return validAnnotations(cevent, ceventInfo.annotations())
                            .flatMap(annotations -> {
                                Set<EventAttrValuePair> notPresent = updatePresentEventAttrs(cevent, annotations);
                                Set<EventAttr> attrsToAdd = addEventAttrs(cevent, notPresent);

                                cevent.getEventAttrs().addAll(attrsToAdd);
                                cevent.setVisitNumber(ceventInfo.vnumber());
                                cevent.setActivityStatus(newStatus);
                                CollectionEvent savedCevent = collectionEventRepository.saveAndFlush(cevent);

                                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                                eventPublisher.publishVisitUpdated(username, pnumber, vnumber);
                                return Either.right(CollectionEventDTO.fromCollectionEvent(savedCevent));
                            });
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
                        eventPublisher.publishVisitRead(auth.getName(), cevent.pnumber(), cevent.vnumber());
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
            .withExtras(new ArrayList<>(attributes.values()), new ArrayList<>(sourceSpecimens.values()));

        return Either.right(cevent);
    }

    /**
     * Fails fast at the moment
     *
     * Could be converted to list all errors
     */
    private Either<AppError, List<EventAttrValuePair>> validAnnotations(
        CollectionEvent cevent,
        List<AnnotationDTO> annotations
    ) {
        List<EventAttrValuePair> result = new ArrayList<>();

        for (StudyEventAttr studyAttr : cevent.getPatient().getStudy().getStudyEventAttrs()) {
            String label = studyAttr.getGlobalEventAttr().getLabel();
            Optional<AnnotationDTO> annotationMaybe = annotations
                .stream()
                .filter(a -> label.equals(a.name()))
                .findFirst();

            if (annotationMaybe.isEmpty()) {
                return Either.left(new ValidationError("annotation not found: %s".formatted(label)));
            }

            if (!studyAttr.getActivityStatus().equals(Status.ACTIVE)) {
                return Either.left(new ValidationError("annotation not active: %s".formatted(label)));
            }

            EventAttrValuePair pair = new EventAttrValuePair();
            pair.studyAttr = studyAttr;
            pair.value = annotationMaybe.get().value();

            if (studyAttr.getRequired() && pair.value.isBlank()) {
                return Either.left(new ValidationError("annotation value is required: %s".formatted(label)));
            }

            String type = studyAttr.getGlobalEventAttr().getEventAttrType().getName();

            if (!pair.value.isBlank()) {
                if (type.equals("number") && !StringUtil.isNumeric(pair.value)) {
                    return Either.left(new ValidationError("annotation value is not numeric: %s".formatted(label)));
                }

                if (type.equals("date_time")) {
                    try {
                        DateUtil.parseDateTime(pair.value);
                    } catch (ParseException ex) {
                        return Either.left(
                            new ValidationError("annotation value is not date time: %s".formatted(label))
                        );
                    }
                }

                String permissible = studyAttr.getPermissible();
                if (type.contains("select_") && !permissible.isBlank() && pair.value != null && !pair.value.isBlank()) {
                    List<String> validValues = List.of(permissible.split(StringUtil.MUTIPLE_VALUES_DELIMITER));

                    for (String value : pair.value.split(StringUtil.MUTIPLE_VALUES_DELIMITER)) {
                        if (!validValues.contains(value)) {
                            return Either.left(
                                new ValidationError(
                                    "invalid annotation value for '%s', not one of: %s'".formatted(
                                            studyAttr.getGlobalEventAttr().getLabel(),
                                            String.join(",", validValues)
                                        )
                                )
                            );
                        }
                    }
                }
            }

            result.add(pair);
        }

        return Either.right(result);
    }

    // returns the annotations that were not present in the collection event
    private Set<EventAttrValuePair> updatePresentEventAttrs(
        CollectionEvent cevent,
        List<EventAttrValuePair> annotations
    ) {
        Set<EventAttrValuePair> notPresent = new HashSet<>(annotations);
        for (EventAttr attr : cevent.getEventAttrs()) {
            for (EventAttrValuePair pair : annotations) {
                if (attr.getStudyEventAttr().getId() == pair.studyAttr.getId()) {
                    attr.setValue(pair.value);
                    eventAttrRepository.saveAndFlush(attr);
                    notPresent.remove(pair);
                }
            }
        }
        eventAttrRepository.flush();
        return notPresent;
    }

    private Set<EventAttr> addEventAttrs(CollectionEvent cevent, Set<EventAttrValuePair> annotations) {
        Set<EventAttr> eventAttrs = new HashSet<>(annotations.size());
        for (EventAttrValuePair pair : annotations) {
            EventAttr attr = new EventAttr();
            attr.setCollectionEvent(cevent);
            attr.setStudyEventAttr(pair.studyAttr);
            attr.setValue(pair.value);
            eventAttrs.add(eventAttrRepository.saveAndFlush(attr));
        }
        return eventAttrs;
    }
}
