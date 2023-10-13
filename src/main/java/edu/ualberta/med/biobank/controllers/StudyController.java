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

import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.services.StudyService;

@RestController
@RequestMapping("/studies")
@CrossOrigin("*")
public class StudyController {

    Logger logger = LoggerFactory.getLogger(StudyController.class);

    private final StudyService studyService;

    StudyController(StudyService studyService) {
        this.studyService = studyService;

    }

    @GetMapping(name = "")
    public Page<StudyDTO> allPaginated(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int thePage = page.orElse(0);
        int theSize = size.orElse(10);
        Page<StudyDTO> data = studyService.studyPagination(thePage, theSize, null);
        return data;
    }

}
