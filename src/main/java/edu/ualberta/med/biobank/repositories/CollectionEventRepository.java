package edu.ualberta.med.biobank.repositories;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CollectionEventRepository extends JpaRepository<CollectionEvent, Integer> {

    final Logger logger = LoggerFactory.getLogger(CollectionEventCustomRepository.class);

    @EntityGraph(attributePaths = {"comments", "comments.user"})
    public List<CollectionEvent> findByPatientId(Integer id);

    // @EntityGraph(attributePaths = {
    //         "comments",
    //         "comments.user",
    //         "eventAttrs",
    //         "eventAttrs.studyEventAttr",
    //         "eventAttrs.studyEventAttr.globalEventAttr"
    //     })
    // public List<CollectionEvent> findById(Integer id);
}
