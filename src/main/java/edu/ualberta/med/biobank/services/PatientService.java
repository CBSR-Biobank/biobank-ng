package edu.ualberta.med.biobank.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.dtos.CollectionEventSummaryDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientSummaryDTO;
import edu.ualberta.med.biobank.dtos.SpecimenCountsDTO;
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
                    final var counts = collectionEventCustomRepository.collectionEventCountsByPatientId(p.id());
                    var collectionEvents = collectionEventRepository.findByPatientId(p.id())
                        .stream()
                        .map(ce -> toCollectionEventDTO(ce, counts.get(ce.getId())))
                        .toList();
                    return p.withCollectionEvents(collectionEvents);
                });
    }

    public Page<PatientSummaryDTO> patientPagination(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable = null;
        if (sort != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<Patient> data = patientRepository.findAll(pageable);
        return data.map(p -> new PatientSummaryDTO(p.getPnumber(), p.getStudy().getId(), p.getStudy().getNameShort()));
    }

    private static CollectionEventSummaryDTO toCollectionEventDTO(CollectionEvent cevent, SpecimenCountsDTO counts) {
        return new CollectionEventSummaryDTO(
            cevent.getId(),
            cevent.getVisitNumber(),
            counts.specimenCount(),
            counts.aliquotCount(),
            cevent.getActivityStatus().getName()
        );
    }
}
