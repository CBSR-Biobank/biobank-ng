package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.applicationevents.BiobankEventPublisher;
import edu.ualberta.med.biobank.domain.SourceSpecimen;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.dtos.SpecimenDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patient.SpecimenReadPermission;
import edu.ualberta.med.biobank.repositories.SpecimenRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SpecimenService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(SpecimenService.class);

    private SpecimenRepository specimenRepository;

    private BiobankEventPublisher eventPublisher;

    public SpecimenService(SpecimenRepository specimenRepository, BiobankEventPublisher eventPublisher) {
        this.specimenRepository = specimenRepository;
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
                eventPublisher.publishSpecimenRead(auth.getName(), specimen.patientNumber(), inventoryId);
                return specimen;
            });
    }

    public Either<AppError, Collection<AliquotSpecimenDTO>> aliquotsForInventoryId(String parentInventoryId) {
        return findByInventoryId(parentInventoryId)
            .flatMap(specimen -> {
                    logger.info("source specimen: {}", LoggingUtils.prettyPrintJson(specimen));

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
}
