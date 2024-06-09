package edu.ualberta.med.biobank.domain;

import java.util.UUID;

enum TaskState {
    CREATED, RUNNING, COMPLETED, CANCELLED
}

public record Task(UUID id, int progress, TaskState state) {

    public Task toRunning() {
        return new Task(this.id(), this.progress(), TaskState.RUNNING);
    }

    public Task toCompleted() {
        return new Task(this.id(), this.progress(), TaskState.COMPLETED);
    }

    public Task toCancelled() {
        return new Task(this.id(), this.progress(), TaskState.CANCELLED);
    }

    public Task withProgress(int progress) {
        return new Task(this.id(), progress, this.state());
    }

    public boolean isRunning() {
        return state.equals(TaskState.RUNNING);
    }

    public boolean isCompleted() {
        return state.equals(TaskState.COMPLETED);
    }

    public String stateName() {
        return this.state.name();
    }

    public static Task create() {
        return new Task(UUID.randomUUID(), 0, TaskState.CREATED);
    }
}
