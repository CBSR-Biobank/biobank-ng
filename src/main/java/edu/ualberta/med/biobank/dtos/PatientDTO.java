package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import java.util.List;

public record PatientDTO(
    Integer id,
    String pnumber,
    Date createdAt,
    Long specimenCount,
    Long aliquotCount,
    Integer studyId,
    String studyNameShort,
    List<CollectionEventSummaryDTO> collectionEvents
) {
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
