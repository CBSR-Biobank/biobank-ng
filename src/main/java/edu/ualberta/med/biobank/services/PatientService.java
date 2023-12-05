package edu.ualberta.med.biobank.services;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationEvents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.Unauthorized;
import edu.ualberta.med.biobank.permission.patient.PatientReadPermission;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.repositories.PatientRepository;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class PatientService {

    Logger logger = LoggerFactory.getLogger(PatientService.class);

    PatientRepository patientRepository;

    CollectionEventRepository collectionEventRepository;

    private BiobankEventPublisher eventPublisher;

    public PatientService(PatientRepository patientRepository,
        CollectionEventRepository collectionEventRepository, BiobankEventPublisher eventPublisher) {
        this.patientRepository = patientRepository;
        this.collectionEventRepository = collectionEventRepository;
        this.eventPublisher = eventPublisher;
    }


    public void save(Patient patient) {
        patientRepository.save(patient);
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

        var values = found.get();
        var patient = new PatientDTO(
            values.get("id", Integer.class),
            values.get("pnumber", String.class),
            values.get("createdAt", Date.class),
            values.get("specimenCount", Long.class),
            values.get("aliquotCount", Long.class),
            values.get("studyId", Integer.class),
            values.get("studyNameShort", String.class),
            List.of()
        );


        var permission = new PatientReadPermission(patient.studyId());
        var allowedMaybe = permission.isAllowed();
        logger.info("patient study id: {}, allowed: {}", patient.studyId(), allowedMaybe);
        return allowedMaybe
            .flatMap(allowed -> {
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
