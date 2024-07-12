package edu.ualberta.med.biobank.dtos;

import java.util.Optional;
import java.util.UUID;
import edu.ualberta.med.biobank.domain.Task;

public record CatalogueTaskDTO(UUID id, int progress, String state, String nameShort, Optional<String> fileUrl) {
    public static CatalogueTaskDTO fromTask(Task task, String nameShort) {
        Optional<String> fileUrl = Optional.empty();
        if (task.isCompleted()) {
            fileUrl = Optional.of("/api/studies/catalogues/download/%s_%s.xlsx".formatted(nameShort, task.id()));
        }
        return new CatalogueTaskDTO(task.id(), task.progress(), task.stateName(), nameShort, fileUrl);
    }
}
