package edu.ualberta.med.biobank.dtos;

import java.util.Set;

public record PatientSummaryDTO(String pnumber, Integer studyId, String studyNameShort) {
}
