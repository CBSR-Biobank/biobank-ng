package edu.ualberta.med.biobank.applicationevents;

import org.springframework.context.ApplicationEvent;

public class UserLoggedInEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String username;

    public UserLoggedInEvent(Object source, String name) {
        super(source);
        this.username = name;
    }

    public String getUsername() {
        return username;
    }
}
