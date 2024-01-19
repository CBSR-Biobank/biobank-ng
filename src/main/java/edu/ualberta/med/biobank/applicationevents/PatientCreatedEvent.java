package edu.ualberta.med.biobank.applicationevents;

import org.springframework.context.ApplicationEvent;

public class PatientCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String pnumber;

    public PatientCreatedEvent(Object source, String username, String pnumber) {
        super(source);
        this.username = username;
        this.pnumber = pnumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPnumber() {
        return pnumber;
    }
}
