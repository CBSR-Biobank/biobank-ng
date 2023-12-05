package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;
import java.util.HashSet;
import java.util.Set;

public record DomainDTO(
    Integer domainId,
    Boolean allCenters,
    Boolean allStudies,
    Set<Integer> centerIds,
    Set<Integer> studyIds
) {
    public static DomainDTO fromTuple(Tuple data) {
        return new DomainDTO(
            data.get("DOMAIN_ID", Number.class).intValue(),
            data.get("ALL_CENTERS", Boolean.class),
            data.get("ALL_STUDIES", Boolean.class),
            new HashSet<>(),
            new HashSet<>()
        );
    }

    public boolean hasCenter(Integer centerId) {
        return allCenters || centerIds.contains(centerId);
    }

    public boolean hasStudy(Integer studyId) {
        return allStudies || studyIds.contains(studyId);
    }
}
