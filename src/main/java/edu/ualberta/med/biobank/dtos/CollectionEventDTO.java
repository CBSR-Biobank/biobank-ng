package edu.ualberta.med.biobank.dtos;

import java.util.List;

public record CollectionEventDTO(
    Integer id,
    Integer visitNumber,
    String status,
    Integer patientId,
    String patientNumber,
    Integer studyId,
    String studyNameShort,
    List<EventAttributeDTO> attributes,
    List<CommentDTO> comments
) {}
