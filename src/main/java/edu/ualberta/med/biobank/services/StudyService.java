package edu.ualberta.med.biobank.services;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.Operation;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.domain.Task;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.dtos.CatalogueTaskDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenTypeDTO;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.dtos.StudyNameDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.BadRequest;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.Forbidden;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.permission.patients.StudyReadPermission;
import edu.ualberta.med.biobank.repositories.StudyRepository;
import edu.ualberta.med.biobank.services.catalogue.CatalogueCreateOp;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;

@Service
public class StudyService {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyService.class);

    @Value("${biobank.catalogue.folder}")
    private String catalogueFolder;

    private StudyRepository studyRepository;

    private final TaskService taskService;

    UserService userService;

    public StudyService(StudyRepository studyRepository, TaskService taskService, UserService userService) {
        this.studyRepository = studyRepository;
        this.taskService = taskService;
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
    public Either<AppError, List<StudyNameDTO>> studyNames(String... stringStatuses) {
        return Status.statusStringsToIds(stringStatuses).flatMap(statusIds -> {
            Collection<Tuple> names = studyRepository.listNames(statusIds, Tuple.class);
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

    public Either<AppError, List<AnnotationTypeDTO>> annotationTypes(String nameshort, String... stringStatuses) {
        var found = studyRepository.findByNameShort(nameshort, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("study"));
        }

        var study = StudyDTO.fromTuple(found.get());
        return new StudyReadPermission(study.id())
            .isAllowed()
            .flatMap(allowed -> {
                if (!allowed) {
                    return Either.left(new PermissionError("study read permission"));
                }
                return Status.statusStringsToIds(stringStatuses);
            })
            .flatMap(statusIds -> {
                Collection<Tuple> attributeTypes = studyRepository.listAttributes(nameshort, statusIds, Tuple.class);
                var dtos = attributeTypes.stream().map(s -> AnnotationTypeDTO.fromTuple(s)).toList();
                return Either.right(dtos);
            });
    }

    public Either<AppError, List<SourceSpecimenTypeDTO>> sourceSpecimens(String nameshort) {
        var found = studyRepository.findByNameShort(nameshort, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("study"));
        }

        var study = StudyDTO.fromTuple(found.get());
        return new StudyReadPermission(study.id())
            .isAllowed()
            .flatMap(allowed -> {
                if (!allowed) {
                    return Either.left(new PermissionError("study read permission"));
                }

                Collection<Tuple> sourceSpecimenTypes = studyRepository.listSourceSpecimens(nameshort, Tuple.class);
                var dtos = sourceSpecimenTypes.stream().map(s -> SourceSpecimenTypeDTO.fromTuple(s)).toList();
                return Either.right(dtos);
            });
    }

    public CatalogueTaskDTO catalogueCreate(String nameShort) {
        var studyMaybe = findByNameShort(nameShort);
        if (studyMaybe.isLeft()) {
            throw new AppErrorException(studyMaybe.getLeft().get());
        }

        var study = studyMaybe.getRight().get();
        logger.info("catalogue requested for study %s".formatted(study.nameShort()));

        var op = new CatalogueCreateOp(study.nameShort());
        taskService.submit(op);
        return CatalogueTaskDTO.fromTask(op.task(), nameShort);
    }

    public CatalogueTaskDTO catalogueTaskStatus(String nameShort, UUID id) {
        var task = taskService.get(id);
        if (task == null) {
            throw new AppErrorException(new BadRequest("invalid task id"));
        }
        var result = CatalogueTaskDTO.fromTask(task, nameShort);
        if (task.isCompleted()) {
            var pathname = "%s/%s_%s.xlsx".formatted(catalogueFolder, nameShort, id);
            File path = new File(pathname);
            if (!path.exists()) {
                var cancelledTask = task.toCancelled();
                return CatalogueTaskDTO.fromTask(cancelledTask, nameShort);
            }
        }
        return result;
    }

    public void catalogueTaskDelete(UUID id) {
        var task = taskService.get(id);
        if (task == null) {
            throw new AppErrorException(new BadRequest("invalid task id"));
        }
        taskService.cancel(id);
    }
}
