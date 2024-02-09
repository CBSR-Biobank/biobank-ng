package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.Study;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {
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
        order by s.name_short
        """,
        nativeQuery = true
    )
    <T> Collection<T> listNames(Set<Integer> statusValues, Class<T> type);

    @Query(
        value = """
        select
            eat.NAME type,
            gea.LABEL label,
            sea.ACTIVITY_STATUS_ID status,
            sea.REQUIRED required,
            sea.PERMISSIBLE validValues
        from study
        left join study_event_attr sea on sea.STUDY_ID = study.ID
        left join global_event_attr gea on gea.id = sea.GLOBAL_EVENT_ATTR_ID
        left join event_attr_type eat on eat.ID = gea.EVENT_ATTR_TYPE_ID
        where
            study.name_short = :nameshort
            and sea.activity_status_id in :statusValues
        order by gea.label
        """,
        nativeQuery = true
    )
    <T> Collection<T> listAttributes(String nameshort, Set<Integer> statusValues, Class<T> type);

    @Query(
        value = """
        select
            ss.id,
            stype.name,
            stype.name_short nameShort,
            ss.need_original_volume needOriginalVolume
        from source_specimen ss
        left join study on study.id = ss.STUDY_ID
        left join specimen_type stype on stype.id=ss.specimen_type_id
        where study.name_short = :nameshort
        order by stype.name
        """,
        nativeQuery = true
    )
    <T> Collection<T> listSourceSpecimens(String nameshort, Class<T> type);
}
