package edu.ualberta.med.biobank.permission.patients;

import edu.ualberta.med.biobank.ApplicationContextProvider;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.permission.Permission;
import edu.ualberta.med.biobank.services.StudyService;
import edu.ualberta.med.biobank.services.UserService;
import io.jbock.util.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class PatientReadPermission implements Permission {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientReadPermission.class);

    private Integer studyId;

    public PatientReadPermission(Integer studyId) {
        this.studyId = studyId;
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
                    return Either.right(user.hasPermission(PermissionEnum.PATIENT_READ, null, null));
                }

                var studyService = applicationContext.getBean(StudyService.class);

                return studyService
                    .getByStudyId(studyId)
                    .flatMap(study -> Either.right(user.hasPermission(PermissionEnum.PATIENT_READ, null, studyId)));
            });
    }
}
