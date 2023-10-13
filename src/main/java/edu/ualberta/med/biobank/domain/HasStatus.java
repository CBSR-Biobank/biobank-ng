package edu.ualberta.med.biobank.domain;

public interface HasStatus {
    public Status getActivityStatus();

    public void setActivityStatus(Status activityStatus);
}
