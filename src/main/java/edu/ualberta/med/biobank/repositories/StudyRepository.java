package edu.ualberta.med.biobank.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Study;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>{
}
