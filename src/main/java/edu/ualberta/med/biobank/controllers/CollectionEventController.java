package edu.ualberta.med.biobank.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.CollectionEventService;

@RestController
public class CollectionEventController {

    private final CollectionEventService collectionEventService;

    CollectionEventController(CollectionEventService collectionEventService) {
        this.collectionEventService = collectionEventService;
    }

    @RequestMapping("/patients/{pnumber}/collection-events/{vnumber}")
    public CollectionEventDTO get(@PathVariable String pnumber, @PathVariable Integer vnumber) {
        return collectionEventService.findByPnumberAndVnumber(pnumber, vnumber).orElseThrow(err -> {
                return new AppErrorException(err);
        });
    }

}
