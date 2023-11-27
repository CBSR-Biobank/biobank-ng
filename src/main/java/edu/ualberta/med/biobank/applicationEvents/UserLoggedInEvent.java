package edu.ualberta.med.biobank.applicationEvents;

import org.springframework.context.ApplicationEvent;

public class UserLoggedInEvent extends ApplicationEvent {

    private String username;

    public UserLoggedInEvent(Object source, String name) {
        super(source);
        this.username = name;
    }

    public String getUsername() {
        return username;
    }
}
