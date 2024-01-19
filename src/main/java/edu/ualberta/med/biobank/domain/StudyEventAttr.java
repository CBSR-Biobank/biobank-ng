package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "STUDY_EVENT_ATTR")
public class StudyEventAttr extends DomainEntity implements HasStatus {

    @Column(name = "PERMISSIBLE")
    private String permissible;

    // TODO: rename to isRequired
    @Column(name = "REQUIRED")
    private boolean required = false;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.StudyEventAttr.globalEventAttr.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GLOBAL_EVENT_ATTR_ID", nullable = false)
    private GlobalEventAttr globalEventAttr;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.StudyEventAttr.study.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_ID", nullable = false)
    private Study study;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.StudyEventAttr.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status = Status.ACTIVE;

    public String getPermissible() {
        return this.permissible;
    }

    public void setPermissible(String permissible) {
        this.permissible = permissible;
    }

    public boolean getRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public GlobalEventAttr getGlobalEventAttr() {
        return this.globalEventAttr;
    }

    public void setGlobalEventAttr(GlobalEventAttr globalEventAttr) {
        this.globalEventAttr = globalEventAttr;
    }

    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Override
    public Status getActivityStatus() {
        return this.status;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.status = activityStatus;
    }
}
