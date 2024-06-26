package edu.ualberta.med.biobank.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.CommentAddDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.PatientAddDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientUpdateDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Collection;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{pnumber}")
    public PatientDTO getPatient(@PathVariable String pnumber) {
        return patientService.get(pnumber).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{pnumber}/comments")
    public Collection<CommentDTO> getPatientComments(@PathVariable String pnumber) {
        return patientService.getComments(pnumber).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO postPatient(@RequestBody PatientAddDTO patient) {
        return patientService.add(patient).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping(path = "/{pnumber}", consumes = "application/json")
    public PatientDTO putPatient(@PathVariable String pnumber, @RequestBody PatientUpdateDTO data) {
        return patientService.update(pnumber, data).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping(path = "/{pnumber}/comments", consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO postPatientComment(@PathVariable String pnumber, @RequestBody CommentAddDTO comment) {
        return patientService.addComment(pnumber, comment).orElseThrow(err -> new AppErrorException(err));
    }
}
