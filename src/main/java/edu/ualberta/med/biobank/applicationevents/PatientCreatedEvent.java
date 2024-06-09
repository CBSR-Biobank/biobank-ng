package edu.ualberta.med.biobank.applicationevents;

public class PatientCreatedEvent extends BiobankEvent {

    private static final long serialVersionUID = 1L;

    private final String pnumber;

    public PatientCreatedEvent(String username, String pnumber) {
        super(username);
        this.pnumber = pnumber;
    }

    public String getPnumber() {
        return pnumber;
    }
}
