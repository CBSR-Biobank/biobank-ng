package edu.ualberta.med.biobank.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationevents.SpecimenReadEvent;
import edu.ualberta.med.biobank.applicationevents.VisitUpdatedEvent;
import edu.ualberta.med.biobank.domain.Clinic;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.OriginInfo;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.domain.SpecimenRequest;
import edu.ualberta.med.biobank.domain.SpecimenType;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.dtos.ClinicDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenAddDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.dtos.SpecimenDTO;
import edu.ualberta.med.biobank.dtos.SpecimenPullDTO;
import edu.ualberta.med.biobank.dtos.SpecimenTypeDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.BadRequest;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patients.CollectionEventUpdatePermission;
import edu.ualberta.med.biobank.permission.patients.SpecimenReadPermission;
import edu.ualberta.med.biobank.repositories.ClinicRepository;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.repositories.CustomSpecimenRepository;
import edu.ualberta.med.biobank.repositories.OriginInfoRepository;
import edu.ualberta.med.biobank.repositories.SpecimenRepository;
import edu.ualberta.med.biobank.repositories.SpecimenTypeRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import edu.ualberta.med.biobank.util.StringUtil;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class SpecimenService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(SpecimenService.class);

    private final SpecimenRepository specimenRepository;

    private final CollectionEventService collectionEventService;

    private final CollectionEventRepository collectionEventRepository;

    private final ClinicService clinicService;

    private final ClinicRepository clinicRepository;

    private final SpecimenTypeService specimenTypeService;

    private final SpecimenTypeRepository specimenTypeRepository;

    private final OriginInfoRepository originInfoRepository;

    private final CustomSpecimenRepository customSpecimenRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final Validator validator;

    public SpecimenService(
        SpecimenRepository specimenRepository,
        CollectionEventService collectionEventService,
        CollectionEventRepository collectionEventRepository,
        ClinicService clinicService,
        ClinicRepository clinicRepository,
        SpecimenTypeService specimenTypeService,
        SpecimenTypeRepository specimenTypeRepository,
        OriginInfoRepository originInfoRepository,
        CustomSpecimenRepository customSpecimenRepository,
        ApplicationEventPublisher eventPublisher,
        Validator validator
    ) {
        this.specimenRepository = specimenRepository;
        this.collectionEventService = collectionEventService;
        this.collectionEventRepository = collectionEventRepository;
        this.clinicService = clinicService;
        this.clinicRepository = clinicRepository;
        this.specimenTypeService = specimenTypeService;
        this.specimenTypeRepository = specimenTypeRepository;
        this.originInfoRepository = originInfoRepository;
        this.customSpecimenRepository = customSpecimenRepository;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
    }

    public Either<AppError, Specimen> getBySpecimenId(Integer id) {
        return specimenRepository
            .findById(id)
            .map(Either::<AppError, Specimen>right)
            .orElseGet(() -> Either.left(new EntityNotFound("specimen")));
    }

    public Either<AppError, SpecimenDTO> findByInventoryId(String inventoryId) {
        Map<Integer, SpecimenDTO> specimens = new HashMap<>();

        specimenRepository
            .findByInventoryId(inventoryId, Tuple.class)
            .stream()
            .forEach(row -> {
                var specimenId = row.get("specimenId", Integer.class);
                specimens.computeIfAbsent(specimenId, id -> SpecimenDTO.fromTuple(row));
            });

        if (specimens.isEmpty()) {
            return Either.left(new EntityNotFound("invalid inventory ID"));
        }

        var specimen = specimens.values().iterator().next();
        var permission = new SpecimenReadPermission(specimen.studyId());
        return permission
            .isAllowed()
            .flatMap(allowed -> {
                if (!allowed) {
                    return Either.left(new PermissionError("study"));
                }
                return Either.right(specimen);
            })
            .map(aliquots -> {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                var event = new SpecimenReadEvent(auth.getName(), specimen.patientNumber(), inventoryId);
                eventPublisher.publishEvent(event);
                return specimen;
            });
    }

    public Either<AppError, Collection<AliquotSpecimenDTO>> aliquotsForInventoryId(String parentInventoryId) {
        return findByInventoryId(parentInventoryId).flatMap(specimen -> {
            logger.debug("source specimen: {}", LoggingUtils.prettyPrintJson(specimen));

            if (!specimen.isSourceSpecimen()) {
                return Either.left(new ValidationError("not a source specimen"));
            }

            Map<Integer, AliquotSpecimenDTO> specimens = new HashMap<>();
            specimenRepository
                .findByParentInventoryId(parentInventoryId, Tuple.class)
                .stream()
                .forEach(row -> {
                    var specimenId = row.get("specimenId", Integer.class);
                    specimens.computeIfAbsent(specimenId, id -> AliquotSpecimenDTO.fromTuple(row));
                });

            return Either.right(specimens.values());
        });
    }

    public List<SpecimenPullDTO> specimenRequest(List<SpecimenRequest> requests) {
        var results = new ArrayList<SpecimenPullDTO>();
        for (SpecimenRequest request : requests) {
            var pullChoices = customSpecimenRepository.pullChoices(
                request.pnumber(),
                request.dateDrawn(),
                request.specimenType()
            );

            //logger.info("------------->  %s".formatted(LoggingUtils.prettyPrintJson(pullChoices)));

            for (int j = 0; j < request.count(); j++) {
                if (j < pullChoices.size()) {
                    var dto = SpecimenPullDTO.fromSpecimenPull(pullChoices.get(j));
                    results.add(dto);
                }
            }

            if (pullChoices.size() < request.count()) {
                results.add(new SpecimenPullDTO(
                    request.pnumber(),
                    "",
                    request.dateDrawn(),
                    request.specimenType(),
                    "NOT_FOUND(%s)".formatted(request.count() - pullChoices.size()),
                    Status.NONE.toString()));
            }
        }



        return results;
    }

    public Either<AppError, SourceSpecimenDTO> add(SourceSpecimenAddDTO dto) {
        return validateSpecimen(dto).flatMap(info -> {
            var permission = new CollectionEventUpdatePermission(info.visit().studyId(), info.clinic().id());
            return permission
                .isAllowed()
                .flatMap(allowed -> {
                    if (!allowed) {
                        return Either.left(new PermissionError("collection event update"));
                    }

                    Clinic clinic = clinicRepository.getReferenceById(info.clinic().id());
                    CollectionEvent visit = collectionEventRepository.getReferenceById(info.visit().id());
                    SpecimenType specimenType = specimenTypeRepository.getReferenceById(info.specimenType().id());

                    OriginInfo oi = new OriginInfo();
                    oi.setCenter(clinic);
                    originInfoRepository.save(oi);

                    Specimen specimen = new Specimen();
                    specimen.setCurrentCenter(clinic);
                    specimen.setOriginInfo(oi);
                    specimen.setTopSpecimen(specimen);
                    specimen.setActivityStatus(Status.fromName(dto.status()));
                    specimen.setCollectionEvent(visit);
                    specimen.setOriginalCollectionEvent(visit);
                    specimen.setCreatedAt(dto.timeDrawn());
                    specimen.setInventoryId(dto.inventoryId());
                    specimen.setQuantity(dto.quantity());
                    specimen.setPlateErrors(StringUtil.EMPTY_STRING);
                    specimen.setSampleErrors(StringUtil.EMPTY_STRING);
                    specimen.setSpecimenType(specimenType);
                    specimenRepository.save(specimen);

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    eventPublisher.publishEvent(new VisitUpdatedEvent(auth.getName(), dto.pnumber(), dto.vnumber()));
                    return Either.right(SourceSpecimenDTO.fromSpecimen(specimen));
                });
        });
    }

    private Either<AppError, SpecimenAddInfo> validateSpecimen(SourceSpecimenAddDTO specimen) {
        if (specimen == null) {
            throw new IllegalArgumentException("specimen cannot be null");
        }

        Set<ConstraintViolation<SourceSpecimenAddDTO>> violations = validator.validate(specimen);
        if (!violations.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<SourceSpecimenAddDTO> constraintViolation : violations) {
                errors.add(constraintViolation.getMessage());
            }
            return Either.left(new ValidationError(String.join(", ", errors)));
        }

        if (Status.fromName(specimen.status()) == null) {
            return Either.left(new ValidationError("status value is invalid"));
        }
        List<String> errors = new ArrayList<>();

        Either<AppError, SpecimenDTO> specimenMaybe = findByInventoryId(specimen.inventoryId());
        if (specimenMaybe.isRight()) {
            errors.add("specimen already exists");
        }

        Either<AppError, ClinicDTO> clinicMaybe = clinicService.findByNameShortInternal(
            specimen.originCenterNameShort()
        );
        if (clinicMaybe.isLeft()) {
            errors.add(clinicMaybe.getLeft().get().getMessage());
        }

        Either<AppError, CollectionEventDTO> visitMaybe = collectionEventService.getInternal(
            specimen.pnumber(),
            specimen.vnumber()
        );
        if (visitMaybe.isLeft()) {
            errors.add(visitMaybe.getLeft().get().getMessage());
        }

        Either<AppError, SpecimenTypeDTO> specimenTypeMaybe = specimenTypeService.findByNameShort(
            specimen.specimenTypeNameShort()
        );
        if (specimenTypeMaybe.isLeft()) {
            errors.add("specimen type name short not found");
        }

        if (!errors.isEmpty()) {
            return Either.left(new BadRequest(String.join(", ", errors)));
        }

        return Either.right(
            new SpecimenAddInfo(
                clinicMaybe.getRight().get(),
                visitMaybe.getRight().get(),
                specimenTypeMaybe.getRight().get()
            )
        );
    }
}
