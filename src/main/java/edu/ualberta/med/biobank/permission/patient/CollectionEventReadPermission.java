package edu.ualberta.med.biobank.permission.patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.ualberta.med.biobank.ApplicationContextProvider;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.Unauthorized;
import edu.ualberta.med.biobank.permission.Permission;
import edu.ualberta.med.biobank.services.StudyService;
import edu.ualberta.med.biobank.services.UserService;
import io.jbock.util.Either;

public class CollectionEventReadPermission implements Permission {

    private final Logger logger = LoggerFactory.getLogger(CollectionEventReadPermission.class);

    private Integer studyId;

    public CollectionEventReadPermission(Integer studyId) {
        this.studyId = studyId;
    }

    @Override
    public Either<AppError, Boolean> isAllowed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        var userService = applicationContext.getBean(UserService.class);
        var user = userService.findOneWithMemberships(auth.getName());
        var studyService = applicationContext.getBean(StudyService.class);

        return (studyId == null)
            ? Either.right(PermissionEnum.COLLECTION_EVENT_READ.isAllowed(user, null, null))
            : studyService
                .getByStudyId(studyId)
                .flatMap(study -> {
                    var result = PermissionEnum.COLLECTION_EVENT_READ.isAllowed(user, study);
                    if (!result) {
                        return Either.left(new Unauthorized("collectionEvent permission"));
                    }
                    return Either.right(result);
                });
    }
}
