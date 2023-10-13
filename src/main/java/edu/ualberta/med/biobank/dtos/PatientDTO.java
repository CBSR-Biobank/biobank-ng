package edu.ualberta.med.biobank.dtos;

import java.util.List;

public record PatientDTO(String pnumber, Integer studyId, String studyNameShort, List<CollectionEventDTO> collectionEvents) {
}
