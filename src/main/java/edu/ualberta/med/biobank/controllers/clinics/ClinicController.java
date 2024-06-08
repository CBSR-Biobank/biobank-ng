package edu.ualberta.med.biobank.controllers.clinics;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.ClinicDTO;
import edu.ualberta.med.biobank.dtos.ClinicNameDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/api/clinics", produces = "application/json")
public class ClinicController {

    @SuppressWarnings("unused")
    Logger logger = LoggerFactory.getLogger(ClinicController.class);

    private final ClinicService clinicService;

    ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping(name = "")
    public Page<ClinicDTO> allPaginated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        int thePage = page.orElse(0);
        int theSize = size.orElse(10);
        return clinicService.clinicPagination(thePage, theSize, null)
            .orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("names")
    public List<ClinicNameDTO> clinicNames(@RequestParam(required = false) String[] status) {
        return clinicService
            .clinicNames(status)
            .orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("{nameshort}")
    public ClinicDTO get(@PathVariable String nameshort) {
        return clinicService
            .findByNameShort(nameshort)
            .orElseThrow(err -> new AppErrorException(err));
    }

}
