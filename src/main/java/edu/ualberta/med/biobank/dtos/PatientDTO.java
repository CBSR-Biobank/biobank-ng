package edu.ualberta.med.biobank.dtos;

import java.util.List;

public record PatientDTO(
    Integer id,
    String pnumber,
    Integer sourceSpecimenCount,
    Integer aliquotsCount,
    Integer studyId,
    String studyNameShort,
    List<CollectionEventSummaryDTO> collectionEvents
) {}
