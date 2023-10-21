package edu.ualberta.med.biobank.dtos;

import java.util.Date;

public record CollectionEventInfoDTO(
    Integer id,
    Long specimenCount,
    Long aliquotCount,
    Date createdAt
) {}
