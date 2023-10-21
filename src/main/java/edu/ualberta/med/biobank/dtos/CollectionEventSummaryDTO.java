package edu.ualberta.med.biobank.dtos;

import java.util.Date;

public record CollectionEventSummaryDTO(
    Integer id,
    Integer visitNumber,
    Long specimenCount,
    Long aliquotCount,
    Date createdAt,
    String status
) {}
