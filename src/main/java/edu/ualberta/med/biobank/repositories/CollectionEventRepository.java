package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import java.util.List;
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
    final Logger logger = LoggerFactory.getLogger(CollectionEventCustomRepository.class);

    @EntityGraph(attributePaths = { "comments", "comments.user" })
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
            "eventAttrs.studyEventAttr.globalEventAttr"
        }
    )
    public List<CollectionEvent> findAll(Specification<CollectionEvent> spec);
}
