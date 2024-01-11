package edu.ualberta.med.biobank.controllers;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.SpecimenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class SpecimenController {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(SpecimenController.class);


    private final SpecimenService specimenService;

    SpecimenController(SpecimenService specimenService) {
        this.specimenService = specimenService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/specimens/aliquots/{inventoryId}")
    public Collection<AliquotSpecimenDTO> get(@PathVariable String inventoryId) {
        return specimenService.findByParentInventoryId(inventoryId).orElseThrow(err -> {
                return new AppErrorException(err);
        });
    }

}
