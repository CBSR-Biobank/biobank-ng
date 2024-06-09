package edu.ualberta.med.biobank.controllers;

import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.domain.Task;
import edu.ualberta.med.biobank.errors.BadRequest;
import edu.ualberta.med.biobank.exception.AppErrorException;
import edu.ualberta.med.biobank.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

record TaskDTO(UUID id, int progress, String state) {
    public static TaskDTO fromTask(Task task) {
        return new TaskDTO(task.id(), task.progress(), task.stateName());
    }
}

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{id}")
    public TaskDTO get(@PathVariable UUID id) {
        var task = taskService.get(id);
        if (task == null) {
            throw new AppErrorException(new BadRequest("invalid task id"));
        }
        return TaskDTO.fromTask(task);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable UUID id) {
        var task = taskService.get(id);
        if (task == null) {
            throw new AppErrorException(new BadRequest("invalid task id"));
        }
        taskService.cancel(id);
    }

}
