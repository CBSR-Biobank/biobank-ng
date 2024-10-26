package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.SpecimenPull;
import edu.ualberta.med.biobank.domain.Status;
import java.util.Date;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomSpecimenRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String PULL_CHOICES_SQL =
        """
        select
           p.pnumber,
           s.inventory_id,
           stop.created_at date_drawn,
           concat(ct.label, sp.position_string) location,
           styp.name_short specimen_type,
           s.activity_status_id status_id
        from specimen s
           left join collection_event ce on ce.id = s.collection_event_id
           left join patient p on p.id = ce.patient_id
           left join specimen stop on stop.id = s.top_specimen_id
           left join specimen_type styp on styp.id = s.specimen_type_id
           left join specimen_position sp on sp.specimen_id = s.id
           left join container ct on ct.id = sp.container_id
        where ct.label not like "SS%"
           and p.pnumber = ?
           and abs(datediff(stop.created_at, ?)) <= 1
           and styp.name_short = ?
           and s.activity_status_id != 2
        order by
           s.activity_status_id,
           rand()
        """;

    RowMapper<SpecimenPull> pullChoiceRowMapper = (rs, rowNum) -> {
        return new SpecimenPull(
            rs.getString("pnumber"),
            rs.getString("inventory_id"),
            rs.getDate("date_drawn"),
            rs.getString("specimen_type"),
            rs.getString("location"),
            Status.fromId(rs.getInt("status_id"))
        );
    };

    public CustomSpecimenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SpecimenPull> pullChoices(String pnumber, Date dateDrawn, String specimenType) {
        return jdbcTemplate.query(PULL_CHOICES_SQL, pullChoiceRowMapper, pnumber, dateDrawn, specimenType);
    }
}
