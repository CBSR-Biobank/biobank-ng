package edu.ualberta.med.biobank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.EventAttr;

@Repository
public interface EventAttrRepository
    extends JpaRepository<EventAttr, Integer>, JpaSpecificationExecutor<EventAttr> {
}
