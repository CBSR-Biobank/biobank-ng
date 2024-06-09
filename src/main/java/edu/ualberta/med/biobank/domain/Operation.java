package edu.ualberta.med.biobank.domain;

/**
 * Used by the task service to represent a running task.
 *
 */
public abstract class Operation {

    protected Task task;

    public Operation(Task task) {
        this.task = task;
    }

    public Task task() {
        return this.task;
    }
}
