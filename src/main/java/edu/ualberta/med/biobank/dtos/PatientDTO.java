package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import java.util.List;

public record PatientDTO(
    Integer id,
    String pnumber,
    Date createdAt,
    Integer specimenCount,
    Integer aliquotCount,
    Integer studyId,
    String studyNameShort,
    List<CollectionEventSummaryDTO> collectionEvents
) {
    public Integer getId() {
        return id;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public PatientDTO withCollectionEvents(List<CollectionEventSummaryDTO> collectionEvents) {
        return new PatientDTO(
            id,
            pnumber,
            createdAt,
            specimenCount,
            aliquotCount,
            studyId,
            studyNameShort,
            collectionEvents
        );
    }
}
