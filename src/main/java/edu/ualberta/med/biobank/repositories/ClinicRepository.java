package edu.ualberta.med.biobank.repositories;

import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Clinic;

// see JPA repository with single table inheritance (hibernate)
//     https://stackoverflow.com/a/63658452

@Repository
public interface ClinicRepository extends CenterRepository<Clinic> {}
