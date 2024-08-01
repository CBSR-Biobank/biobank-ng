package edu.ualberta.med.biobank.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.ValidationError;
import edu.ualberta.med.biobank.util.LoggingUtils;
import io.jbock.util.Either;

@Component
public class ScheduledTasks {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Value("${biobank.catalogue.folder}")
    private String catalogueFolder;

    @Scheduled(cron = "0 0 3 * * ?")
    public void runAt3AM() {
        logger.info("Task performed at {}", LocalDateTime.now());
        var result = removeCatalogueFiles();
        if (result.isLeft()) {
            logger.error("task error: %s".formatted(LoggingUtils.prettyPrintJson(result.getLeft().get())));
        }
    }

    // @Scheduled(fixedRate = 5000)
    // public void performTask() {
    //     logger.info("Task performed at {}", LocalDateTime.now());
    //     var result = removeCatalogueFiles();
    //     if (result.isLeft()) {
    //         logger.error("task error: %s".formatted(LoggingUtils.prettyPrintJson(result.getLeft().get())));
    //     }
    // }

    private Either<AppError, Boolean> removeCatalogueFiles() {
        Instant currentTime = Instant.now();
        File directory = new File(catalogueFolder);

        if (!directory.exists() || !directory.isDirectory()) {
            return Either.left(new ValidationError("catalogues directory does not exist"));
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return Either.left(new ValidationError("cannot list files in catalogues directory"));
        }

        for (File file : files) {
            if (!file.isFile()) {
                return Either.left(new ValidationError("invalid file in catalogues directory"));
            }

            try {
                Path filePath = Paths.get(file.getAbsolutePath());
                BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                Instant fileTime = attr.lastModifiedTime().toInstant();

                if (fileTime.isBefore(currentTime.minus(24, ChronoUnit.HOURS))) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        return Either.left(new ValidationError("failed to delete file in catalogues directory"));
                    }
                    logger.info("Deleted: " + file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Either.right(true);
    }
}
