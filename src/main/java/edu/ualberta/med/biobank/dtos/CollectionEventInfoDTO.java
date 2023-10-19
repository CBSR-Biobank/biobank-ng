package edu.ualberta.med.biobank.dtos;

import java.util.Date;

public record CollectionEventInfoDTO(
    Integer collectionEventId,
    Integer specimenCount,
    Integer aliquotCount,
    Date createdAt
) {}
