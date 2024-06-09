package edu.ualberta.med.biobank.services.catalogue;

import edu.ualberta.med.biobank.domain.Operation;
import edu.ualberta.med.biobank.domain.Task;

public class CatalogueCreateOp extends Operation {
    private String studyNameShort;

    public CatalogueCreateOp(String studyNameShort) {
        super(Task.create());
        this.studyNameShort = studyNameShort;
    }

    public String studyNameShort() {
        return studyNameShort;
    }
}
