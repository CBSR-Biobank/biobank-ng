package edu.ualberta.med.biobank.permission.centers;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import edu.ualberta.med.biobank.ApplicationContextProvider;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.permission.Permission;
import edu.ualberta.med.biobank.services.UserService;
import io.jbock.util.Either;

public class SiteReadPermission implements Permission {

    private Integer siteId = null;

    public SiteReadPermission(Integer siteId) {
        this.siteId = siteId;
    }

    @Override
    public Either<AppError, Boolean> isAllowed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        var userService = applicationContext.getBean(UserService.class);
        return userService
            .findOneWithMemberships(auth.getName())
            .flatMap(user -> {
                return Either.right(user.hasPermission(PermissionEnum.SITE_READ, null, siteId));
            });
    }
}
