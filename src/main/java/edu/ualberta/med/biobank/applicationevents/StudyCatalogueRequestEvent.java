package edu.ualberta.med.biobank.applicationevents;

public class StudyCatalogueRequestEvent extends BiobankEvent {

    private static final long serialVersionUID = 1L;

    private String studyNameShort;

    public StudyCatalogueRequestEvent(String username, String studyNameShort) {
        super(username);
        this.studyNameShort = studyNameShort;
    }

    public String getStudyNameShort() {
        return studyNameShort;
    }
}
