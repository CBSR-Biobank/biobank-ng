package edu.ualberta.med.biobank.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientSummaryDTO;
import edu.ualberta.med.biobank.exception.NotFoundException;
import edu.ualberta.med.biobank.services.PatientService;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/{pnumber}")
    public PatientDTO get(@PathVariable String pnumber) {
        return patientService.findByPnumber(pnumber).orElseThrow(err -> {
                return new NotFoundException(err);
        });
    }

    @GetMapping("")
    public Page<PatientSummaryDTO> allPaginated(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int thePage = page.orElse(0);
        int theSize = size.orElse(10);
        var data = patientService.patientPagination(thePage, theSize, null);
        return data;
    }

}
