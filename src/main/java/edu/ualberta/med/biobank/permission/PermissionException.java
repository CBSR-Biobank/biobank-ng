package edu.ualberta.med.biobank.permission;

import edu.ualberta.med.biobank.domain.Center;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.Study;

// TODO: find a better home?
public class PermissionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final PermissionEnum permission;
    private final String centerName;
    private final String studyName;

    public PermissionException(PermissionEnum permission, Center center,
        Study study) {
        this.permission = permission;
        this.centerName = center.getNameShort();
        this.studyName = study.getNameShort();
    }

    public PermissionEnum getPermission() {
        return permission;
    }

    public String getCenter() {
        return centerName;
    }

    public String getStudy() {
        return studyName;
    }
}
