package edu.ualberta.med.biobank.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import edu.ualberta.med.biobank.domain.SpecimenPull;
import edu.ualberta.med.biobank.domain.SpecimenRequest;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenAddDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.errors.BadRequest;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.SpecimenService;
import edu.ualberta.med.biobank.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/specimens")
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

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("request")
    public Collection<SpecimenPull> request(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppErrorException(new BadRequest("the file is empty"));
        }

        var requested = new ArrayList<SpecimenRequest>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            var csvFormat = CSVFormat.DEFAULT.builder().build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);

            for (CSVRecord record : records) {
                requested.add(new SpecimenRequest(
                    record.get(0),
                    DateUtil.parseDate(record.get(1)),
                    record.get(2),
                    Integer.parseInt(record.get(3))
                ));
            }
            return specimenService.specimenRequest(requested);
        } catch (Exception e) {
            throw new AppErrorException(new BadRequest(e.getMessage()));
        }
    }

}
