package edu.ualberta.med.biobank.dtos;

import java.util.List;

public record CollectionEventDTO(Integer visitNumber, String status, List<EventAttributeDTO> attributes) {
}
