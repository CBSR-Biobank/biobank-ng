package edu.ualberta.med.biobank.applicationevents;

import org.springframework.context.ApplicationEvent;

public class SpecimenReadEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String pnumber;
    private final String inventoryId;

    public SpecimenReadEvent(Object source, String username, String pnumber, String inventoryId) {
        super(source);
        this.username = username;
        this.pnumber = pnumber;
        this.inventoryId = inventoryId;
    }

    public String getUsername() {
        return username;
    }

    public String getPnumber() {
        return pnumber;
    }

    public String getInventoryId() {
        return inventoryId;
    }
}
