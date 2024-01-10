package edu.ualberta.med.biobank.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{pnumber}")
    public PatientDTO get(@PathVariable String pnumber) {
        return patientService.findByPnumber(pnumber).orElseThrow(err -> {
                return new AppErrorException(err);
        });
    }

}
