package edu.ualberta.med.biobank.applicationevents;

public class SpecimenReadEvent extends BiobankEvent {

    private static final long serialVersionUID = 1L;

    private final String pnumber;
    private final String inventoryId;

    public SpecimenReadEvent(String username, String pnumber, String inventoryId) {
        super(username);
        this.pnumber = pnumber;
        this.inventoryId = inventoryId;
    }


    public String getPnumber() {
        return pnumber;
    }

    public String getInventoryId() {
        return inventoryId;
    }
}
