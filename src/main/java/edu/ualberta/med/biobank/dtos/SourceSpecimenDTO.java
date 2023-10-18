package edu.ualberta.med.biobank.dtos;

import java.math.BigDecimal;
import java.util.Date;

public record SourceSpecimenDTO(
    Integer id,
    String inventoryId,
    Integer specimenTypeId,
    String specimenTypeNameShort,
    Date timeDrawn,
    BigDecimal quantity,
    String status,
    Integer originCenterId,
    String originCenterNameShort,
    Integer currentCenterId,
    String currentCenterNameShort,
    Boolean hasComments
) {
}
