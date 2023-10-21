package edu.ualberta.med.biobank.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.ualberta.med.biobank.dtos.CollectionEventInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class CollectionEventCustomRepository {

    final Logger logger = LoggerFactory.getLogger(CollectionEventCustomRepository.class);

    private static final String CEVENT_COUNTS_BY_PATIENT_ID_QRY =
        "SELECT new edu.ualberta.med.biobank.dtos.CollectionEventInfoDTO(" +
        "    cevent.id," +
        "    COUNT(DISTINCT sourceSpecimens)," +
        "    COUNT(DISTINCT allSpecimens) - COUNT(DISTINCT sourceSpecimens)," +
        "    MIN(sourceSpecimens.createdAt)" +
        " )"  +
        " FROM CollectionEvent cevent" +
        " LEFT JOIN cevent.originalSpecimens sourceSpecimens" +
        " LEFT JOIN cevent.allSpecimens allSpecimens" +
        " WHERE cevent.patient.id = :patientId" +
        " GROUP BY cevent.id";

    @PersistenceContext
    private EntityManager em;

    // returns a map of the collection event counts for a patient
    //
    // the keys of the map are the collection event IDs
    public Map<Integer, CollectionEventInfoDTO> collectionEventCountsByPatientId(Integer patientId) {
        TypedQuery<CollectionEventInfoDTO> q = em.createQuery(CEVENT_COUNTS_BY_PATIENT_ID_QRY, CollectionEventInfoDTO.class);
        q.setParameter("patientId", patientId);
        List<CollectionEventInfoDTO> dtos = q.getResultList();

        var result = new HashMap<Integer, CollectionEventInfoDTO>();
        for (var dto : dtos) {
            result.put(dto.id(), dto);
        }
        return result;
    }
}
