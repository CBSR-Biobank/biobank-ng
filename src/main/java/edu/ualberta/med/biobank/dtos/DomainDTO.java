package edu.ualberta.med.biobank.dtos;

import java.util.Set;

public record DomainDTO(Integer domainId, Boolean allCenters, Boolean allStudies, Set<Integer> centerIds, Set<Integer> studyIds) {

    public boolean hasCenter(Integer centerId) {
        return centerIds.contains(centerId);
    }

    public boolean hasStudy(Integer studyId) {
        return studyIds.contains(studyId);
    }
}
