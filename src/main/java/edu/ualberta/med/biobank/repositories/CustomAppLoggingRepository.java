package edu.ualberta.med.biobank.repositories;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.dtos.AppLogEntryDTO;

@Repository
public class CustomAppLoggingRepository {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(CustomAppLoggingRepository.class);

    private JdbcTemplate jdbcTemplate;

    private static final String PAGING_BASE_QRY = "from Log where log.created_at is not null ";

    private static final String PAGING_TOTAL_SQL = "select count(*) " + PAGING_BASE_QRY;

    private static final String PAGING_SQL =
        """
        select
            log.id,
            log.username,
            log.created_at,
            log.center,
            log.action,
            log.patient_number,
            log.inventory_id,
            log.location_label,
            log.details,
            log.type
        """
        + PAGING_BASE_QRY
        + """
        order by log.created_at desc
        limit ?
        offset ?
        """;

    RowMapper<AppLogEntryDTO> rowMapper = (rs, rowNum) -> {
        return new AppLogEntryDTO(
            rs.getInt("id"),
            rs.getTimestamp("created_at"),
            rs.getString("username"),
            rs.getString("center"),
            rs.getString("action"),
            rs.getString("patient_number"),
            rs.getString("inventory_id"),
            rs.getString("location_label"),
            rs.getString("details"),
            rs.getString("type")
        );
    };

    public CustomAppLoggingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<AppLogEntryDTO> paginated(Pageable pageable) {
        if (pageable == null || !pageable.isPaged()) {
            pageable = PageRequest.of(0, 10);
        }

        var total = jdbcTemplate.queryForObject(PAGING_TOTAL_SQL, Integer.class);
        var result = jdbcTemplate.query(PAGING_SQL, rowMapper, pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<AppLogEntryDTO>(result, pageable, total);

    }
}
