package edu.ualberta.med.biobank.permission.patients;

import edu.ualberta.med.biobank.ApplicationContextProvider;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.dtos.ClinicDTO;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.permission.Permission;
import edu.ualberta.med.biobank.services.ClinicService;
import edu.ualberta.med.biobank.services.StudyService;
import edu.ualberta.med.biobank.services.UserService;
import io.jbock.util.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CollectionEventUpdatePermission implements Permission {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventUpdatePermission.class);

    private static final PermissionEnum PERMISSION = PermissionEnum.COLLECTION_EVENT_CREATE;

    private Integer studyId;

    private Integer centerId;

    public CollectionEventUpdatePermission(Integer studyId) {
        this.studyId = studyId;
    }

    public CollectionEventUpdatePermission(Integer studyId, Integer centerId) {
        this.studyId = studyId;
        this.centerId = centerId;
    }

    @Override
    public Either<AppError, Boolean> isAllowed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        var userService = applicationContext.getBean(UserService.class);

        return userService
            .findOneWithMemberships(auth.getName())
            .flatMap(user -> {
                if (studyId == null) {
                    return Either.right(user.hasPermission(PERMISSION, null, null));
                }

                StudyService studyService = applicationContext.getBean(StudyService.class);
                ClinicService clinicService = applicationContext.getBean(ClinicService.class);

                Either<AppError, StudyDTO> studyMaybe = studyService.getByStudyId(studyId);
                if (studyMaybe.isLeft()) {
                    return studyMaybe.map(null);
                }

                if (centerId != null) {
                    Either<AppError, ClinicDTO> centerMaybe = clinicService.getByClinicId(centerId);

                    if (centerMaybe.isLeft()) {
                        return centerMaybe.map(null);
                    }
                    return Either.right(user.hasPermission(PERMISSION, centerId, studyId));
                }

                return Either.right(user.hasPermission(PERMISSION, null, studyId));
            });
    }
}
