package edu.ualberta.med.biobank.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_2__UserApiKey extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("DROP TABLE IF EXISTS USER_API_KEY");

            var sql =
                """
                  CREATE TABLE USER_API_KEY (
                    ID integer NOT NULL auto_increment,
                    USER_ID integer NOT NULL,
                    API_KEY varchar(40) NOT NULL,
                    PRIMARY KEY (ID),
                    UNIQUE KEY `API_KEY` (`API_KEY`),
                    CONSTRAINT `UK_7qf2tl5mch5s2hm6kv9xh7irb` FOREIGN KEY (`USER_ID`) REFERENCES `principal` (`ID`)
                        ON DELETE CASCADE ON UPDATE NO ACTION
                  ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
                """;
            statement.execute(sql);
        }
    }
}
