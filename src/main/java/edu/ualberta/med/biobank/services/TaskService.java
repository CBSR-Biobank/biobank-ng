package edu.ualberta.med.biobank.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.domain.Operation;
import edu.ualberta.med.biobank.domain.Task;

@Service
public class TaskService {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);


    private ApplicationEventPublisher publisher;

    private Map<UUID, Task> tasks = new ConcurrentHashMap<>();

    public TaskService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Task get(UUID id) {
        return tasks.get(id);
    }

    public void submit(Operation op) {
        tasks.computeIfAbsent(op.task().id(), id -> op.task());
        publisher.publishEvent(op);
    }

    public void start(UUID id) {
        tasks.computeIfPresent(id, (_id, task) -> task.toRunning());
    }

    public void progress(UUID id, int progress) {
        tasks.computeIfPresent(id, (_id, task) -> task.withProgress(progress));
    }

    public void cancel(UUID id) {
        tasks.computeIfPresent(id, (_id, task) -> task.toCancelled());
    }

    public Boolean active(UUID id)  {
        var task = tasks.get(id);
        if (task == null) {
            throw new IllegalArgumentException("Task %s not found".formatted(id.toString()));
        }
        return task.isRunning();
    }

    public void complete(UUID id) {
        tasks.computeIfPresent(id, (_id, task) -> task.toCompleted());
    }

    public void shutdown() {
        tasks.values().stream().forEach(task -> {
            tasks.computeIfPresent(task.id(), (_id, t) -> t.toCancelled());
        });
    }

    @EventListener
    public void shutdownSubscriber(ContextClosedEvent event) {
        logger.info("received app shutdown event");
        this.shutdown();
    }
}
