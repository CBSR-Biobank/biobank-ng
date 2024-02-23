package edu.ualberta.med.biobank.repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.SpecimenType;

@Repository
public interface SpecimenTypeRepository extends JpaRepository<SpecimenType, Integer> {
    @Query(
        value = """
        select
            st.id,
            st.name,
            st.name_short nameShort
        from specimen_type st
        where
            st.name_short = :nameShort
        """,
        nativeQuery = true
    )
    public <T> Collection<T> findByNameShort(String nameShort, Class<T> type);
}
