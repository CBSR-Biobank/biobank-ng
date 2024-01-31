package edu.ualberta.med.biobank.applicationevents;

import org.springframework.context.ApplicationEvent;

public class VisitUpdatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String pnumber;
    private final Integer vnumber;

    public VisitUpdatedEvent(Object source, String username, String pnumber, Integer vnumber) {
        super(source);
        this.username = username;
        this.pnumber = pnumber;
        this.vnumber = vnumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPnumber() {
        return pnumber;
    }

    public Integer getVnumber() {
        return vnumber;
    }
}
