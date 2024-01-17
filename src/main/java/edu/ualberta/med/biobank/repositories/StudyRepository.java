package edu.ualberta.med.biobank.repositories;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Study;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer>{
    @Query(
        value = """
                select
                    s.id,
                    s.name,
                    s.name_short nameShort,
                    s.activity_status_id activityStatusId,
                    s.version
                from study s
                where s.id = :studyId
                """,
        nativeQuery = true
    )
    <T> Collection<T> findById(Number studyId, Class<T> type);

    @Query(
        value = """
                select
                    s.id,
                    s.name,
                    s.name_short nameShort,
                    s.activity_status_id activityStatusId,
                    s.version
                from study s
                where s.name_short = :nameshort
                """,
        nativeQuery = true
    )
    <T> Collection<T> findByNameShort(String nameshort, Class<T> type);

    @Query(
        value = """
                select
                    s.id,
                    s.name,
                    s.name_short nameShort,
                    s.activity_status_id activityStatusId,
                    s.version
                from study s
                """,
        nativeQuery = true
    )
    <T> Page<T> findAll(Pageable pageable, Class<T> type);

    @Query(
        value = """
                select
                    s.id,
                    s.name,
                    s.name_short nameShort,
                    s.activity_status_id activityStatusId,
                    s.version
                from study s
                where s.id in :studyIds
                """,
        nativeQuery = true
    )
    <T> Page<T> findByIds(Pageable pageable, Set<Integer> studyIds, Class<T> type);

    @Query(
        value = """
                select
                    s.id,
                    s.name name,
                    s.name_short nameShort
                from study s
                where s.activity_status_id in :statusValues
                """,
        nativeQuery = true
    )
    <T> Collection<T> getNames(Set<Integer> statusValues, Class<T> type);

}
