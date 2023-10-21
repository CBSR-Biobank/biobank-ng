package edu.ualberta.med.biobank.repositories;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import io.jbock.util.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

@Repository
public class PatientCustomRepository {

    Logger logger = LoggerFactory.getLogger(PatientCustomRepository.class);

    private static final String FIND_BY_PNUMBER_QRY =
        "SELECT p.id as id," +
        "       p.pnumber as pnumber," +
        "       p.createdAt as createdAt," +
        "       p.study.id as studyId," +
        "       p.study.nameShort as studyNameShort," +
        "       COUNT(DISTINCT sourceSpecs) as spcCount," +
        "       COUNT(DISTINCT allSpecs) - COUNT(DISTINCT sourceSpecs) as alqCount" +
        " FROM Patient p" +
        " JOIN p.study study" +
        " LEFT JOIN p.collectionEvents ce" +
        " LEFT JOIN ce.originalSpecimens sourceSpecs" +
        " LEFT JOIN ce.allSpecimens allSpecs" +
        " WHERE p.pnumber= :pnumber" +
        " GROUP BY p.id";

    @PersistenceContext
    private EntityManager entityManager;

    // returns an empty list for collection events, the caller can populate these after
    public Either<AppError, PatientDTO> findByPnumber(String pnumber) {
        try {
            var q = entityManager.createQuery(FIND_BY_PNUMBER_QRY, Tuple.class);
            q.setParameter("pnumber", pnumber);
            var result = q.getSingleResult();

            var patientId = result.get("id", Number.class).intValue();
            return Either.right(
                new PatientDTO(
                    patientId,
                    result.get("pnumber", String.class),
                    result.get("createdAt", Date.class),
                    result.get("spcCount", Number.class).intValue(),
                    result.get("alqCount", Number.class).intValue(),
                    result.get("studyId", Number.class).intValue(),
                    result.get("studyNameShort", String.class),
                    List.of()
                )
            );
        } catch (NoResultException e) {
            return Either.left(new EntityNotFound("patient"));
        }
    }
}
