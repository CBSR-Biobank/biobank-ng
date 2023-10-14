package edu.ualberta.med.biobank.services;

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
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientSummaryDTO;
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

    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    public Either<String, PatientDTO> findByPnumber(String pnumber) {
        return patientCustomRepository.findByPnumber(pnumber);
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
}
