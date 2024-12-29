package edu.ualberta.med.biobank.applicationevents;

public class SpecimenPullRequestEvent extends BiobankEvent {

    private static final long serialVersionUID = 1L;

    public SpecimenPullRequestEvent(String username) {
        super(username);
    }
}
