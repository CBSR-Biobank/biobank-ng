package edu.ualberta.med.biobank.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.dtos.StudyNameDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.Forbidden;
import edu.ualberta.med.biobank.errors.Unauthorized;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.permission.patient.StudyReadPermission;
import edu.ualberta.med.biobank.repositories.StudyRepository;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;

@Service
public class StudyService {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyService.class);

    StudyRepository studyRepository;

    UserService userService;

    public StudyService(StudyRepository studyRepository, UserService userService) {
        this.studyRepository = studyRepository;
        this.userService = userService;
    }

    public void save(Study study) {
        studyRepository.save(study);
    }

    public Either<AppError, StudyDTO> getByStudyId(Integer id) {
        var found = studyRepository.findById(id, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("study"));
        }

        var study = StudyDTO.fromTuple(found.get());

        var permission = new StudyReadPermission(id);
        var allowed = permission.isAllowed();
        return allowed.map(a -> study);
    }

    public Either<AppError, StudyDTO> findByNameShort(String nameshort) {
        var found = studyRepository.findByNameShort(nameshort, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("study"));
        }

        var study = StudyDTO.fromTuple(found.get());

        var permission = new StudyReadPermission(study.id());
        var allowed = permission.isAllowed();
        return allowed.map(a -> study);
    }

    public Either<AppError, Page<StudyDTO>> studyPagination(Integer pageNumber, Integer pageSize, String sort) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService
            .findOneWithMemberships(auth.getName())
            .map(user -> {
                Pageable pageable = sort != null
                    ? PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort)
                    : PageRequest.of(pageNumber, pageSize);

                logger.debug("studyPagination: user: {}", auth.getName());
                Page<Tuple> studyData;

                if (user.hasAllStudies()) {
                    studyData = studyRepository.findAll(pageable, Tuple.class);
                } else {
                    var userStudyIds = user.studyIds();
                    logger.debug("studyPagination: ids: {}", LoggingUtils.prettyPrintJson(userStudyIds));
                    studyData = studyRepository.findByIds(pageable, userStudyIds, Tuple.class);
                }

                return studyData.map(d -> StudyDTO.fromTuple(d));
            })
            .mapLeft(err -> new Forbidden("invalid user"));
    }

    /**
     * Returns study name information.
     *
     * @param status Zero or more statuses to filter the studies by. If null, then all studies are returned.
     *
     * @return A left sided {@link Either} if an error was encountered.
     *
     * @see {@link StudyNameDTO}
     */
    public Either<AppError, List<StudyNameDTO>> studyNames(String... status) {
        return Status
            .fromStrings(status)
            .flatMap(statuses -> {
                Set<Integer> statusIds = new HashSet<>();
                if (status != null) {
                    statusIds.addAll(statuses.stream().map(s -> s.getId()).toList());
                } else {
                    statusIds.addAll(Status.valuesList().stream().map(s -> s.getId()).toList());
                }

                var names = studyRepository.getNames(statusIds, Tuple.class);
                if (names.isEmpty()) {
                    return Either.left(new EntityNotFound("study names not found"));
                }

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                var user = userService.findOneWithMemberships(auth.getName()).getRight().get();
                var dtos = names.stream().map(s -> StudyNameDTO.fromTuple(s));

                if (!user.hasAllStudies()) {
                    var userStudyIds = user.studyIds();
                    dtos = dtos.filter(s -> userStudyIds.contains(s.id()));
                }

                var result = dtos.toList();
                logger.debug("studyNames: user: {}, num_studies: {}", auth.getName(), result.size());
                return Either.right(result);
            });
    }

    public Either<AppError, List<AnnotationTypeDTO>> annotations() {
        return Either.left(new ValidationError("needs implementation"));
    }
}
