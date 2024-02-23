package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.domain.Clinic;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.ClinicDTO;
import edu.ualberta.med.biobank.dtos.ClinicNameDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.Forbidden;
import edu.ualberta.med.biobank.permission.centers.ClinicReadPermission;
import edu.ualberta.med.biobank.repositories.ClinicRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ClinicService {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(ClinicService.class);

    ClinicRepository clinicRepository;

    UserService userService;

    public ClinicService(ClinicRepository clinicRepository, UserService userService) {
        this.clinicRepository = clinicRepository;
        this.userService = userService;
    }

    public void save(Clinic clinic) {
        clinicRepository.save(clinic);
    }

    public Either<AppError, ClinicDTO> getByClinicId(Integer id) {
        var found = clinicRepository.findClinicById(id, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("clinic"));
        }

        ClinicDTO clinic = ClinicDTO.fromTuple(found.get());
        ClinicReadPermission permission = new ClinicReadPermission(clinic.id());
        var allowed = permission.isAllowed();
        return allowed.map(a -> clinic);
    }

    public Either<AppError, ClinicDTO> findByNameShort(String nameshort) {
        return findByNameShortInternal(nameshort).flatMap(clinic -> {
            ClinicReadPermission permission = new ClinicReadPermission(clinic.id());
            var allowed = permission.isAllowed();
            return allowed.map(a -> clinic);
        });
    }

    public Either<AppError, Page<ClinicDTO>> clinicPagination(Integer pageNumber, Integer pageSize, String sort) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService
            .findOneWithMemberships(auth.getName())
            .map(user -> {
                Pageable pageable = sort != null
                    ? PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort)
                    : PageRequest.of(pageNumber, pageSize);

                logger.debug("clinicPagination: user: {}", auth.getName());
                Page<Tuple> clinicData;

                if (user.hasAllCenters()) {
                    clinicData = clinicRepository.findAllClinics(pageable, Tuple.class);
                } else {
                    var userClinicIds = user.centerIds();
                    logger.debug("clinicPagination: ids: {}", LoggingUtils.prettyPrintJson(userClinicIds));
                    clinicData = clinicRepository.findClinicsByIds(pageable, userClinicIds, Tuple.class);
                }

                return clinicData.map(d -> ClinicDTO.fromTuple(d));
            })
            .mapLeft(err -> new Forbidden("invalid user"));
    }

    /**
     * Returns clinic name information.
     *
     * @param status Zero or more statuses to filter the studies by. If null, then all studies are returned.
     *
     * @return A left sided {@link Either} if an error was encountered.
     *
     * @see {@link ClinicNameDTO}
     */
    public Either<AppError, List<ClinicNameDTO>> clinicNames(String... stringStatuses) {
        return Status.statusStringsToIds(stringStatuses).flatMap(statusIds -> {
            Collection<Tuple> names = clinicRepository.listClinicNames(statusIds, Tuple.class);
            if (names.isEmpty()) {
                return Either.left(new EntityNotFound("clinic names not found"));
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var user = userService.findOneWithMemberships(auth.getName()).getRight().get();
            var dtos = names.stream().map(s -> ClinicNameDTO.fromTuple(s));

            if (!user.hasAllStudies()) {
                var userClinicIds = user.centerIds();
                dtos = dtos.filter(s -> userClinicIds.contains(s.id()));
            }

            var result = dtos.toList();
            logger.debug("clinicNames: user: {}, num_studies: {}", auth.getName(), result.size());
            return Either.right(result);
        });
    }

    Either<AppError, ClinicDTO> findByNameShortInternal(String nameshort) {
        var found = clinicRepository.findClinicByNameShort(nameshort, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("clinic short name not found"));
        }

        return Either.right(ClinicDTO.fromTuple(found.get()));
    }
}
