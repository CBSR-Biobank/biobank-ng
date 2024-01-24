package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import java.util.List;
import edu.ualberta.med.biobank.domain.Patient;
import jakarta.persistence.Tuple;

public record PatientDTO(
    Integer id,
    String pnumber,
    Date createdAt,
    Long specimenCount,
    Long aliquotCount,
    Long commentCount,
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
            commentCount,
            studyId,
            studyNameShort,
            collectionEvents
        );
    }

    public static PatientDTO fromPatient(Patient patient) {
        return new PatientDTO(
            patient.getId(),
            patient.getPnumber(),
            patient.getCreatedAt(),
            0L,
            0L,
            Long.valueOf(patient.getComments().size()),
            patient.getStudy().getId(),
            patient.getStudy().getNameShort(),
            List.of()
        );
    }

    public static PatientDTO fromTuple(Tuple data) {
        return new PatientDTO(
            data.get("id", Integer.class),
            data.get("pnumber", String.class),
            data.get("createdAt", Date.class),
            data.get("specimenCount", Long.class),
            data.get("aliquotCount", Long.class),
            data.get("commentCount", Long.class),
            data.get("studyId", Integer.class),
            data.get("studyNameShort", String.class),
            List.of()
        );
    }
}
