package edu.ualberta.med.biobank.services;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationevents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.PatientCreateDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patient.PatientCreatePermission;
import edu.ualberta.med.biobank.permission.patient.PatientReadPermission;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.repositories.PatientRepository;
import edu.ualberta.med.biobank.repositories.StudyRepository;
import io.jbock.util.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class PatientService {

    Logger logger = LoggerFactory.getLogger(PatientService.class);

    @PersistenceContext
    protected EntityManager entityManager;

    private PatientRepository patientRepository;

    private CollectionEventRepository collectionEventRepository;

    private BiobankEventPublisher eventPublisher;

    private StudyRepository studyRepository;

    private UserService userService;

    private Validator validator;

    public PatientService(
        PatientRepository patientRepository,
        CollectionEventRepository collectionEventRepository,
        StudyRepository studyRepository,
        UserService userService,
        Validator validator,
        BiobankEventPublisher eventPublisher
    ) {
        this.patientRepository = patientRepository;
        this.collectionEventRepository = collectionEventRepository;
        this.studyRepository = studyRepository;
        this.userService = userService;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
    }

    public Either<AppError, PatientDTO> save(PatientCreateDTO dto) {
        var violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<PatientCreateDTO> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            return Either.left(new ValidationError(sb.toString()));
        }

        var found = studyRepository.findByNameShort(dto.studyNameShort(), Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("study with short name not found: %s".formatted(dto.studyNameShort())));
        }

        var studyId = found.get().get("id", Integer.class);
        var permission = new PatientCreatePermission(studyId);
        var allowedMaybe = permission.isAllowed();
        return allowedMaybe
            .flatMap(allowed -> {
                logger.debug("patient study id: {}, allowed: {}", studyId, allowed);
                if (!allowed) {
                    return Either.left(new PermissionError("study"));
                }
                return Either.right(allowed);
            })
            .flatMap(allowed -> {
                var study = studyRepository.getReferenceById(studyId);

                Patient patient = new Patient();
                patient.setPnumber(dto.pnumber());
                patient.setCreatedAt(dto.createdAt());
                patient.setStudy(study);
                Patient savedPatient = patientRepository.save(patient);
                entityManager.flush();

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                eventPublisher.publishPatientCreated(auth.getName(), dto.pnumber());
                return Either.right(PatientDTO.fromPatient(savedPatient));
            });
    }

    public Either<AppError, Patient> getByPatientId(Integer id) {
        return patientRepository
            .findById(id)
            .map(Either::<AppError, Patient>right)
            .orElseGet(() -> Either.left(new EntityNotFound("patient")));
    }

    public Either<AppError, PatientDTO> findByPnumber(String pnumber) {
        var found = patientRepository.findByPnumber(pnumber, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("patient"));
        }

        var patient = PatientDTO.fromTuple(found.get());

        var permission = new PatientReadPermission(patient.studyId());
        var allowedMaybe = permission.isAllowed();
        return allowedMaybe
            .flatMap(allowed -> {
                logger.debug("patient study id: {}, allowed: {}", patient.studyId(), allowed);
                if (!allowed) {
                    return Either.left(new PermissionError("study"));
                }
                return Either.right(patient);
            })
            .map(p -> {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                eventPublisher.publishPatientRead(auth.getName(), pnumber);

                final var ceSummary = collectionEventRepository
                    .findSummariesByPatient(pnumber, Tuple.class)
                    .stream()
                    .map(row -> CollectionEventSummaryDTO.fromTuple(row))
                    .toList();

                return p.withCollectionEvents(ceSummary);
            });
    }
}
