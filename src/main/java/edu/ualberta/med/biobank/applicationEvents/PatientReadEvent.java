package edu.ualberta.med.biobank.applicationEvents;

import org.springframework.context.ApplicationEvent;

public class PatientReadEvent extends ApplicationEvent {

    private final String username;
    private final String pnumber;

    public PatientReadEvent(Object source, String username, String pnumber) {
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
