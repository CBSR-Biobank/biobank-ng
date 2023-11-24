package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import edu.ualberta.med.biobank.domain.Status;

public record CollectionEventSummaryDTO(
    Integer id,
    Integer visitNumber,
    Long specimenCount,
    Long aliquotCount,
    Date createdAt,
    Status status
) {}
