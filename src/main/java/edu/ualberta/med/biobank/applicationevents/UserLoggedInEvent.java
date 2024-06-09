package edu.ualberta.med.biobank.applicationevents;

public class UserLoggedInEvent extends BiobankEvent {

    private static final long serialVersionUID = 1L;

    public UserLoggedInEvent(String username) {
        super(username);
    }

}
