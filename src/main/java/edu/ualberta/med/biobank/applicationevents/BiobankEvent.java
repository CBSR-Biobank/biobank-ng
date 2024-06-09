package edu.ualberta.med.biobank.applicationevents;

public class BiobankEvent {

    private static final long serialVersionUID = 1L;

    private final String username;

    public BiobankEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
