package edu.ualberta.med.biobank.services.catalogue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.dtos.AliquotDTO;
import edu.ualberta.med.biobank.repositories.SpecimenRepository;
import edu.ualberta.med.biobank.services.TaskService;
import jakarta.persistence.Tuple;

/**
 * This class implements the catalogue generation as a long running service.
 */
@Service
class StudyCatalogueService {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(StudyCatalogueService.class);

    @Value("${biobank.catalogue.folder}")
    private String catalogueFolder;

    private TaskService taskService;

    private SpecimenRepository specimenRepository;

    public StudyCatalogueService(TaskService taskService, SpecimenRepository specimenRepository) {
        this.taskService = taskService;
        this.specimenRepository = specimenRepository;
    }

    @Async
    @EventListener
    public void catalogue(CatalogueCreateOp op) {
        logger.info("Starting task %s..".formatted(op.task().id()));
        taskService.start(op.task().id());

        Map<String, AliquotDTO> specimens = new LinkedHashMap<>();
        specimenRepository
            .findByStudy(op.studyNameShort(), Tuple.class)
            .stream()
            .forEach(row -> {
            var inventoryId = row.get("inventory_Id", String.class);
            specimens.computeIfAbsent(inventoryId, id -> AliquotDTO.fromTuple(row));
        });

        taskService.progress(op.task().id(), 50);

        try {
            var filename = "%s/%s_%s.xlsx".formatted(catalogueFolder, op.studyNameShort(), op.task().id());
            logger.info(">>>> writing to %s".formatted(filename));
            createCatalogueFolderIfNotExist();
            var writer = new StudyCatalogueWriter(specimens.values());
            writer.write(filename);
            taskService.progress(op.task().id(), 100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("task finished %s..".formatted(op.task().id()));
        taskService.complete(op.task().id());
    }

    private void createCatalogueFolderIfNotExist() {
        var path = Paths.get(catalogueFolder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("An error occurred while creating the folder: " + e.getMessage());
            }
        }
    }
}
