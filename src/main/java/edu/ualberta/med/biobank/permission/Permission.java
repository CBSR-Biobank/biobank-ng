package edu.ualberta.med.biobank.permission;

import edu.ualberta.med.biobank.errors.AppError;
import io.jbock.util.Either;

/**
 * Implementations of this interface should follow the template
 * "{noun}{verb}{noun}..Permission," for example, CreateSpecimenPermission NOT
 * SpecimenCreatePermission.
 *
 * @author jferland
 */
public interface Permission {
    public Either<AppError, Boolean> isAllowed();
}
