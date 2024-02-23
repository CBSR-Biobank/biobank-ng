package edu.ualberta.med.biobank.repositories;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import edu.ualberta.med.biobank.domain.Center;

// see JPA repository with single table inheritance (hibernate)
//     https://stackoverflow.com/a/63658452

@NoRepositoryBean
public interface CenterRepository<S extends Center> extends JpaRepository<S, Integer> {
    @Query(
        value = """
        select
            c.id,
            c.name,
            c.name_short nameShort,
            c.activity_status_id activityStatusId,
            c.version
        from center c
        where
            c.id = :clinicId
            and c.discriminator = 'Clinic'
        """,
        nativeQuery = true
    )
    <T> Collection<T> findClinicById(Number clinicId, Class<T> type);

    @Query(
        value = """
        select
            c.id,
            c.name,
            c.name_short nameShort,
            c.activity_status_id activityStatusId,
            c.version
        from center c
        where
            c.name_short = :nameshort
            and c.discriminator = 'Clinic'
        """,
        nativeQuery = true
    )
    <T> Collection<T> findClinicByNameShort(String nameshort, Class<T> type);

    @Query(
        value = """
        select
            c.id,
            c.name,
            c.name_short nameShort,
            c.activity_status_id activityStatusId,
            c.version
        from center c
        where
            c.discriminator = 'Clinic'
        """,
        nativeQuery = true
    )
    <T> Page<T> findAllClinics(Pageable pageable, Class<T> type);

    @Query(
        value = """
        select
            c.id,
            c.name,
            c.name_short nameShort,
            c.activity_status_id activityStatusId,
            c.version
        from center c
        where
            c.id in :clinicIds
            and c.discriminator = 'Clinic'
        """,
        nativeQuery = true
    )
    <T> Page<T> findClinicsByIds(Pageable pageable, Set<Integer> clinicIds, Class<T> type);

    @Query(
        value = """
        select
            c.id,
            c.name name,
            c.name_short nameShort
        from center c
        where
            c.activity_status_id in :statusValues
            and c.discriminator = 'Clinic'
        order by c.name_short
        """,
        nativeQuery = true
    )
    <T> Collection<T> listClinicNames(Set<Integer> statusValues, Class<T> type);
}
