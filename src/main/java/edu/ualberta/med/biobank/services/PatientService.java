package edu.ualberta.med.biobank.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.EventAttributeDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientSummaryDTO;
import edu.ualberta.med.biobank.repositories.PatientRepository;

@Service
public class PatientService {

    Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    PatientRepository patientRepository;

    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    public Optional<PatientDTO> findById(Integer id) {
        return patientRepository.findById(id).map(p -> patientToDto(p));
    }

    public Optional<PatientDTO> findByPnumber(String pnumber) {
        List<Patient> result = patientRepository.findByPnumber(pnumber);
        if (result.size() != 1) {
            return Optional.empty();
        }

        Patient p = result.get(0);
        return Optional.of(patientToDto(p));
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

    private static PatientDTO patientToDto(Patient patient) {
        var ceDtos = patient
                .getCollectionEvents()
                .stream()
                .map(ce -> {
                    var attrDtos = ce
                            .getEventAttrs()
                            .stream()
                            .map(ea -> new EventAttributeDTO(ea.getStudyEventAttr().getGlobalEventAttr().getLabel(),
                                    ea.getValue()))
                            .toList();
                    return new CollectionEventDTO(ce.getVisitNumber(), ce.getActivityStatus().getName(), attrDtos);
                })
                .toList();
        return new PatientDTO(
                patient.getPnumber(),
                patient.getStudy().getId(),
                patient.getStudy().getNameShort(),
                ceDtos);
    }
}
