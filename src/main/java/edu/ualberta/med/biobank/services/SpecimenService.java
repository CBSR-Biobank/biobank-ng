package edu.ualberta.med.biobank.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.permission.patient.SpecimenReadPermission;
import edu.ualberta.med.biobank.repositories.SpecimenRepository;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;

@Service
public class SpecimenService {

    final Logger logger = LoggerFactory.getLogger(SpecimenService.class);

    SpecimenRepository specimenRepository;

    public SpecimenService(SpecimenRepository specimenRepository) {
        this.specimenRepository = specimenRepository;
    }

    public Either<AppError, Specimen> getBySpecimenId(Integer id) {
        return specimenRepository
            .findById(id)
            .map(Either::<AppError, Specimen>right)
            .orElseGet(() -> Either.left(new EntityNotFound("specimen")));
    }

    public Either<AppError, Collection<AliquotSpecimenDTO>> findByParentInventoryId(String parentInventoryId) {
        Map<Integer, AliquotSpecimenDTO> specimens = new HashMap<>();

        specimenRepository
            .findByParentInventoryId(parentInventoryId, Tuple.class)
            .stream()
            .forEach(row -> {
                var specimenId = row.get("specimenId", Integer.class);
                specimens.computeIfAbsent(specimenId, id -> AliquotSpecimenDTO.fromTuple(row));
            });

        var permission = new SpecimenReadPermission(null);
        return permission.isAllowed().map(allowed -> specimens.values());
    }
}
