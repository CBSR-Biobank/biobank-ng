package edu.ualberta.med.biobank.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import java.sql.Statement;

public class V1_1__Indices extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement update = context.getConnection().createStatement()) {
            // logging table
            update.execute("ALTER TABLE log ADD INDEX log_created_at_ndx (created_at)");
            update.execute("ALTER TABLE log ADD INDEX log_patient_number_ndx (patient_number)");
            update.execute("ALTER TABLE log ADD INDEX log_inventory_id_ndx (inventory_id)");

            // patient table
            update.execute("ALTER TABLE patient ADD INDEX patient_created_at_ndx (created_at)");

            // specimen table
            update.execute("ALTER TABLE specimen ADD INDEX specimen_created_at_ndx (created_at)");

            update.execute("ALTER TABLE specimen_position ADD INDEX spos_container_specimen_index (container_id,specimen_id)");
        }
    }
}
