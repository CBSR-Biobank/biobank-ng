package edu.ualberta.med.biobank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.OriginInfo;

@Repository
public interface OriginInfoRepository extends JpaRepository<OriginInfo, Integer> {

}
