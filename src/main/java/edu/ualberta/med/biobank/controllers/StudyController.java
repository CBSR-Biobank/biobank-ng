package edu.ualberta.med.biobank.controllers;

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
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.dtos.StudyNameDTO;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/studies", produces = "application/json")
public class StudyController {

    @SuppressWarnings("unused")
    Logger logger = LoggerFactory.getLogger(StudyController.class);

    private final StudyService studyService;

    StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping(name = "")
    public Page<StudyDTO> allPaginated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        int thePage = page.orElse(0);
        int theSize = size.orElse(10);
        return studyService.studyPagination(thePage, theSize, null)
            .orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("names")
    public List<StudyNameDTO> studyNames(@RequestParam(required = false) String[] status) {
        return studyService
            .studyNames(status)
            .orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("{nameshort}")
    public StudyDTO get(@PathVariable String nameshort) {
        return studyService
            .findByNameShort(nameshort)
            .orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{nameshort}/annotation-types")
    public List<AnnotationTypeDTO> annotationTypes(@PathVariable String nameshort, @RequestParam(required = false) String[] status) {
        return studyService
            .annotationTypes(nameshort, status)
            .orElseThrow(err -> new AppErrorException(err));
    }
}
