package edu.ualberta.med.biobank.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.dtos.CollectionEventInfoDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.permission.patient.PatientReadPermission;
import edu.ualberta.med.biobank.repositories.CollectionEventCustomRepository;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.repositories.PatientCustomRepository;
import edu.ualberta.med.biobank.repositories.PatientRepository;
import io.jbock.util.Either;

@Service
public class PatientService {

    Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientCustomRepository patientCustomRepository;

    @Autowired
    CollectionEventRepository collectionEventRepository;

    @Autowired
    CollectionEventCustomRepository collectionEventCustomRepository;

    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    public Either<AppError, Patient> getByPatientId(Integer id) {
        return patientRepository.findById(id)
            .map(Either::<AppError, Patient>right)
            .orElseGet(() -> Either.left(new EntityNotFound("patient")));
    }

    public Either<AppError, PatientDTO> findByPnumber(String pnumber) {
        return patientCustomRepository.findByPnumber(pnumber)
            .flatMap(patient -> {
                    var permission = new PatientReadPermission(patient.studyId());
                    var allowed = permission.isAllowed();
                    return allowed.map(a -> patient);
                })
            .map(p -> {
                    final var info = collectionEventCustomRepository.collectionEventCountsByPatientId(p.id());
                    var collectionEvents = collectionEventRepository.findByPatientId(p.id())
                        .stream()
                        .map(ce -> toCollectionEventDTO(ce, info.get(ce.getId())))
                        .toList();
                    return p.withCollectionEvents(collectionEvents);
                });
    }

    private static CollectionEventSummaryDTO toCollectionEventDTO(CollectionEvent cevent, CollectionEventInfoDTO info) {
        if (info == null) {
            throw new RuntimeException("info is null");
        }

        return new CollectionEventSummaryDTO(
            cevent.getId(),
            cevent.getVisitNumber(),
            info.specimenCount(),
            info.aliquotCount(),
            info.createdAt(),
            cevent.getActivityStatus().getName()
        );
    }
}
