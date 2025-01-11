package edu.ualberta.med.biobank.permission.patients;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import edu.ualberta.med.biobank.ApplicationContextProvider;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.permission.Permission;
import edu.ualberta.med.biobank.services.UserService;
import io.jbock.util.Either;

public class StudyReadPermission implements Permission {

    private Integer studyId = null;

    public StudyReadPermission(Integer studyId) {
        this.studyId = studyId;
    }

    @Override
    public Either<AppError, Boolean> isAllowed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        var userService = applicationContext.getBean(UserService.class);
        return userService
            .findOneWithMemberships(auth.getName())
            .flatMap(user -> Either.right(user.hasPermission(PermissionEnum.STUDY_READ, null, studyId)));
    }
}
