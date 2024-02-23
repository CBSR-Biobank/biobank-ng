package edu.ualberta.med.biobank.services;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.dtos.SpecimenTypeDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.repositories.SpecimenTypeRepository;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;

@Service
class SpecimenTypeService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(SpecimenTypeService.class);

    private SpecimenTypeRepository specimenTypeRepository;

    public SpecimenTypeService(SpecimenTypeRepository specimenTypeRepository) {
        this.specimenTypeRepository = specimenTypeRepository;
    }

    public Either<AppError, SpecimenTypeDTO> findByNameShort(String nameShort) {
        Map<Integer, SpecimenTypeDTO> specimenTypes = new HashMap<>();

        specimenTypeRepository
            .findByNameShort(nameShort, Tuple.class)
            .stream()
            .forEach(row -> {
                var specimenTypeId = row.get("id", Integer.class);
                specimenTypes.computeIfAbsent(specimenTypeId, id -> SpecimenTypeDTO.fromTuple(row));
            });

        if (specimenTypes.isEmpty()) {
            return Either.left(new EntityNotFound("invalid name short"));
        }

        if (specimenTypes.size() > 1) {
            return Either.left(new EntityNotFound("more than one name short found"));
        }

        return Either.right(specimenTypes.values().iterator().next());
    }
}
