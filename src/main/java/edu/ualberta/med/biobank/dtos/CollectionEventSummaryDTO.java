package edu.ualberta.med.biobank.dtos;

import java.util.List;

public record CollectionEventSummaryDTO(
    Integer id,
    Integer visitNumber,
    Integer specimenCount,
    Integer aliquotCount,
    String status,
    List<CommentDTO> comments
) {}
