package edu.ualberta.med.biobank.dtos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Status;
import jakarta.persistence.Tuple;

public record CollectionEventDTO(
    Integer id,
    Integer vnumber,
    String status,
    Integer patientId,
    String pnumber,
    Integer studyId,
    String studyNameShort,
    Long commentCount,
    List<AnnotationDTO> annotations,
    List<SourceSpecimenDTO> sourceSpecimens
) {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CollectionEventDTO.class);

    public CollectionEventDTO withExtras(
        List<AnnotationDTO> annotations,
        List<SourceSpecimenDTO> sourceSpecimens
    ) {
        return new CollectionEventDTO(
            id,
            vnumber,
            status,
            patientId,
            pnumber,
            studyId,
            studyNameShort,
            commentCount,
            annotations,
            sourceSpecimens
        );
    }

    public static CollectionEventDTO fromTuple(Tuple data) {
        return new CollectionEventDTO(
            data.get("id", Number.class).intValue(),
            data.get("visitNumber", Number.class).intValue(),
            Status.fromId(data.get("ACTIVITY_STATUS_ID", Integer.class)).getName(),
            data.get("patientId", Number.class).intValue(),
            data.get("patientNumber", String.class),
            data.get("studyId", Number.class).intValue(),
            data.get("studyNameShort", String.class),
            data.get("commentCount", Long.class),
            List.of(),
            List.of()
        );
    }

    public static CollectionEventDTO fromCollectionEvent(CollectionEvent cevent) {
        Map<Integer, AnnotationDTO> attrsById = new HashMap<>();
        cevent
            .getPatient()
            .getStudy()
            .getStudyEventAttrs()
            .forEach(attr -> {
                attrsById.computeIfAbsent(
                    attr.getId(),
                    id ->
                        new AnnotationDTO(
                            attr.getGlobalEventAttr().getEventAttrType().getName(),
                            attr.getGlobalEventAttr().getLabel(),
                            ""
                        )
                );
            });

        cevent
            .getEventAttrs()
            .stream()
            .forEach(attr -> {
                Integer id = attr.getStudyEventAttr().getId();
                attrsById.computeIfPresent(id, (k, v) -> v.withValue(attr.getValue()));
            });

        return new CollectionEventDTO(
            cevent.getId(),
            cevent.getVisitNumber(),
            cevent.getActivityStatus().getName(),
            cevent.getPatient().getId(),
            cevent.getPatient().getPnumber(),
            cevent.getPatient().getStudy().getId(),
            cevent.getPatient().getStudy().getNameShort(),
            Long.valueOf(cevent.getComments().size()),
            attrsById.values().stream().toList(),
            cevent
                .getOriginalSpecimens()
                .stream()
                .filter(specimen -> specimen.getParentSpecimen() == null)
                .map(specimen -> SourceSpecimenDTO.fromSpecimen(specimen))
                .toList()
        );
    }
}
