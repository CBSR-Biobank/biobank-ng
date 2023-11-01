package edu.ualberta.med.biobank.domain.util;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.OptimizerFactory;
import org.hibernate.id.enhanced.TableGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inherits from {@link TableGenerator} with the following changes:
 * <p>
 * <ul>
 * <li>Force the use of the {@link OptimizerFactory.POOL} strategy, where the
 * value shown in the database is the next legal value to use. That is, the
 * value is the low end of the increment size, as opposed to the high end.</li>
 * <li>Force the segment value default to be the entity's table name (note that
 * this can still be overridden).</li>
 * <li>If there is no existing next value in the database for the given segment,
 * use one more than the current maximum id. If the table is empty, then use the
 * configured initial value.</li>
 * </ul>
 *
 * See property "spring.jpa.properties.hibernate.id.optimizer.pooled.preferred" in application.properties.
 *
 * @author Jonathan Ferland
 */
public class CustomIdGenerator implements IdentifierGenerator {

    private final Logger logger = LoggerFactory.getLogger(CustomIdGenerator.class);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String query = String.format(
            "select coalesce(max(%s) + 1) from %s",
            session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
            obj.getClass().getSimpleName()
        );

        logger.info(">>>>>>>>>>>>> generate: {}", query);

        Integer id = session.createQuery(query, Integer.class).getSingleResultOrNull();
        if (id == null) {
            return 1;
        }
        return id;
    }
}
