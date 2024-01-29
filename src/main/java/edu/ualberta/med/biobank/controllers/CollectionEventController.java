package edu.ualberta.med.biobank.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.CollectionEventAddDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.CollectionEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class CollectionEventController {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventController.class);


    private final CollectionEventService collectionEventService;

    CollectionEventController(CollectionEventService collectionEventService) {
        this.collectionEventService = collectionEventService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping(path = "/patients/{pnumber}/collection-events", consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionEventDTO post(@PathVariable String pnumber, @RequestBody CollectionEventAddDTO data) {
        return collectionEventService.addCollectionEvent(pnumber, data).orElseThrow(err -> {
                return new AppErrorException(err);
        });
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/patients/{pnumber}/collection-events/{vnumber}")
    public CollectionEventDTO get(@PathVariable String pnumber, @PathVariable Integer vnumber) {
        return collectionEventService.findByPnumberAndVnumber(pnumber, vnumber).orElseThrow(err -> {
                return new AppErrorException(err);
        });
    }

}
