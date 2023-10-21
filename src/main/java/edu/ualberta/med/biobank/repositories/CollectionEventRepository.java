package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.dtos.CollectionEventInfoDTO;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionEventRepository
    extends JpaRepository<CollectionEvent, Integer>, JpaSpecificationExecutor<CollectionEvent> {

    //@EntityGraph(attributePaths = { "comments", "comments.user" })
    public List<CollectionEvent> findByPatientId(Integer id);

    @EntityGraph(
        attributePaths = {
            "patient",
            "patient.study",
            "patient.study.researchGroup",
            "comments",
            "comments.user",
            "eventAttrs",
            "eventAttrs.studyEventAttr",
            "eventAttrs.studyEventAttr.study",
            "eventAttrs.studyEventAttr.study.researchGroup",
            "eventAttrs.studyEventAttr.globalEventAttr",
            "originalSpecimens",
            "originalSpecimens.specimenType",
            "originalSpecimens.originInfo",
            "originalSpecimens.originInfo.center",
            "originalSpecimens.comments"
        }
    )
    public List<CollectionEvent> findAll(Specification<CollectionEvent> spec);
}
