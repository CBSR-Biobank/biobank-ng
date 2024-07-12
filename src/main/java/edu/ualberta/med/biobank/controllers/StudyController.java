package edu.ualberta.med.biobank.controllers;

import edu.ualberta.med.biobank.applicationevents.StudyCatalogueDownloadEvent;
import edu.ualberta.med.biobank.applicationevents.StudyCatalogueRequestEvent;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.dtos.CatalogueTaskDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenTypeDTO;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.dtos.StudyNameDTO;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.StudyService;
import edu.ualberta.med.biobank.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "/api/studies", produces = "application/json")
public class StudyController {

    @Value("${biobank.catalogue.folder}")
    private String catalogueFolder;

    @SuppressWarnings("unused")
    Logger logger = LoggerFactory.getLogger(StudyController.class);

    private final StudyService studyService;

    private ApplicationEventPublisher eventPublisher;

    StudyController(StudyService studyService, ApplicationEventPublisher eventPublisher) {
        this.studyService = studyService;
        this.eventPublisher = eventPublisher;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping(name = "")
    public Page<StudyDTO> allPaginated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        int thePage = page.orElse(0);
        int theSize = size.orElse(10);
        return studyService.studyPagination(thePage, theSize, null).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("names")
    public List<StudyNameDTO> studyNames(@RequestParam(required = false) String[] status) {
        return studyService.studyNames(status).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("{nameshort}")
    public StudyDTO get(@PathVariable String nameshort) {
        return studyService.findByNameShort(nameshort).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{nameshort}/annotation-types")
    public List<AnnotationTypeDTO> annotationTypes(
        @PathVariable String nameshort,
        @RequestParam(required = false) String[] status
    ) {
        return studyService.annotationTypes(nameshort, status).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{nameshort}/source-specimen-types")
    public List<SourceSpecimenTypeDTO> sourceSpecimens(@PathVariable String nameshort) {
        return studyService.sourceSpecimens(nameshort).orElseThrow(err -> new AppErrorException(err));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/catalogues/{nameshort}")
    public ResponseEntity<String> catlogue(@PathVariable String nameshort) {
        var task = studyService.catalogueCreate(nameshort);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        eventPublisher.publishEvent(new StudyCatalogueRequestEvent(auth.getName(), nameshort));
        return ResponseEntity
            .created(URI.create("/api/studies/catalogue/%s/%s".formatted(nameshort, task.id())))
            .build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/catalogues/{nameshort}/{id}")
    public CatalogueTaskDTO get(@PathVariable String nameshort, @PathVariable UUID id) {
        var result = studyService.catalogueTaskStatus(nameshort, id);
        return result;
    }

    @DeleteMapping("/catalogues/{nameshort}/{id}")
    public void cancel(@PathVariable String nameshort, @PathVariable UUID id) {
        studyService.catalogueTaskDelete(id);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/catalogues/download/{name}")
    public ResponseEntity<?> downloadCatalogue(@PathVariable(value = "name") String fileName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        eventPublisher.publishEvent(new StudyCatalogueDownloadEvent(auth.getName(), fileName));

        var fileMaybe = downloadFile(fileName);
        if (fileMaybe.isEmpty()) {
            throw new AppErrorException(new EntityNotFound("no such file"));
        }
        var file = fileMaybe.get();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    private Optional<Resource> downloadFile(String fileName) {
        File dir = new File(catalogueFolder + "/" + fileName);
        try {
            if (dir.exists()) {
                Resource resource = new UrlResource(dir.toURI());
                return Optional.of(resource);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }
}
