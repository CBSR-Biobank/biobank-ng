package edu.ualberta.med.biobank.dtos;

public record CollectionEventSummaryDTO(
    Integer id,
    Integer visitNumber,
    Integer specimenCount,
    Integer aliquotCount,
    String status
) {}
