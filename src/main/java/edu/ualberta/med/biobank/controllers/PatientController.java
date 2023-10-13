package edu.ualberta.med.biobank.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientSummaryDTO;
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
        Optional<PatientDTO> maybe = patientService.findByPnumber(pnumber);
        if (!maybe.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "patient not found");
        }
        return maybe.get();
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
