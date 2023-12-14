package edu.ualberta.med.biobank.repositories;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Log;
import jakarta.persistence.Tuple;

@Repository
public interface LoggingRepository extends JpaRepository<Log, Integer>{
    @Query(
        value = """
                select
                    log.ID id,
                    log.USERNAME username,
                    log.CREATED_AT createdAt,
                    log.CENTER center,
                    log.ACTION action,
                    log.PATIENT_NUMBER patientNumber,
                    log.INVENTORY_ID inventoryId,
                    log.LOCATION_LABEL locationLabel,
                    log.DETAILS details,
                    log.TYPE type
                from log
                order by log.id
                """,
        nativeQuery = true
    )
    Page<Tuple> findAllWithPagination(Pageable pageable);

    @Query(
        value = """
                select
                    log.ID id,
                    log.USERNAME username,
                    log.CREATED_AT createdAt,
                    log.CENTER center,
                    log.ACTION action,
                    log.PATIENT_NUMBER patientNumber,
                    log.INVENTORY_ID inventoryId,
                    log.LOCATION_LABEL locationLabel,
                    log.DETAILS details,
                    log.TYPE type
                    from log
                order by log.created_at desc
                limit 30
                """,
        nativeQuery = true
    )
    Collection<Tuple> getLastest();
}
