package edu.ualberta.med.biobank.controllers;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.LoggingDTO;
import edu.ualberta.med.biobank.services.LoggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/logs")
@CrossOrigin("*")
public class LoggingController {

    Logger logger = LoggerFactory.getLogger(StudyController.class);

    private final LoggingService loggingService;

    LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;

    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping(name = "")
    public Page<LoggingDTO> allPaginated(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int thePage = page.orElse(0);
        int theSize = size.orElse(10);
        Page<LoggingDTO> data = loggingService.loggingPagination(thePage, theSize, null);
        return data;
    }

}
