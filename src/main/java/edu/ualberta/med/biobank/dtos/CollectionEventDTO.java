package edu.ualberta.med.biobank.dtos;

import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;
import java.util.List;

public record CollectionEventDTO(
    Integer id,
    Integer visitNumber,
    Status status,
    Integer patientId,
    String patientNumber,
    Integer studyId,
    String studyNameShort,
    List<EventAttributeDTO> attributes,
    List<CommentDTO> comments,
    List<SourceSpecimenDTO> sourceSpecimens
) {
    public static CollectionEventDTO fromTuple(Tuple data) {
        return new CollectionEventDTO(
            data.get("id", Number.class).intValue(),
            data.get("visitNumber", Number.class).intValue(),
            Status.fromId(data.get("ACTIVITY_STATUS_ID", Integer.class)),
            data.get("patientId", Number.class).intValue(),
            data.get("patientNumber", String.class),
            data.get("studyId", Number.class).intValue(),
            data.get("studyNameShort", String.class),
            List.of(),
            List.of(),
            List.of()
        );
    }

    public CollectionEventDTO withExtras(
        List<EventAttributeDTO> attributes,
        List<CommentDTO> comments,
        List<SourceSpecimenDTO> sourceSpecimens
    ) {
        return new CollectionEventDTO(
            id,
            visitNumber,
            status,
            patientId,
            patientNumber,
            studyId,
            studyNameShort,
            attributes,
            comments,
            sourceSpecimens
        );
    }
}
