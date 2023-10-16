package edu.ualberta.med.biobank.dtos;

import java.util.List;

public record PatientDTO(
    Integer id,
    String pnumber,
    Integer specimenCount,
    Integer aliquotCount,
    Integer studyId,
    String studyNameShort,
    List<CollectionEventSummaryDTO> collectionEvents
) {
    public Integer getId() {
        return id;
    }

    public PatientDTO withCollectionEvents(List<CollectionEventSummaryDTO> collectionEvents) {
        return new PatientDTO(
            id,
            pnumber,
            specimenCount,
            aliquotCount,
            studyId,
            studyNameShort,
            collectionEvents
        );
    }
}
