package edu.ualberta.med.biobank.applicationevents;

public class StudyCatalogueDownloadEvent extends BiobankEvent {

    private static final long serialVersionUID = 1L;

    private String filename;

    public StudyCatalogueDownloadEvent(String username, String filename) {
        super(username);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
