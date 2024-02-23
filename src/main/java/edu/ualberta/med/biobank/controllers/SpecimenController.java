package edu.ualberta.med.biobank.controllers;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenAddDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.SpecimenService;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/specimens")
public class SpecimenController {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(SpecimenController.class);


    private final SpecimenService specimenService;

    SpecimenController(SpecimenService specimenService) {
        this.specimenService = specimenService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("{inventoryId}/aliquots")
    public Collection<AliquotSpecimenDTO> get(@PathVariable String inventoryId) {
        return specimenService.aliquotsForInventoryId(inventoryId).orElseThrow(err -> {
                return new AppErrorException(err);
        });
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SourceSpecimenDTO postSpecimen(@RequestBody SourceSpecimenAddDTO specimen) {
        return specimenService.add(specimen).orElseThrow(err -> new AppErrorException(err));
    }

}
