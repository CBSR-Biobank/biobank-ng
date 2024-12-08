package edu.ualberta.med.biobank.dtos;

import java.util.Optional;
import java.util.UUID;
import edu.ualberta.med.biobank.domain.Task;

public record SpecimenRequestTaskDTO(UUID id, int progress, String state, Optional<String> fileUrl) {
    public static SpecimenRequestTaskDTO fromTask(Task task, String nameShort) {
        Optional<String> fileUrl = Optional.empty();
        if (task.isCompleted()) {
            fileUrl = Optional.of("/api/studies/catalogues/download/%s_%s.xlsx".formatted(nameShort, task.id()));
        }
        return new SpecimenRequestTaskDTO(task.id(), task.progress(), task.stateName(), fileUrl);
    }
}
