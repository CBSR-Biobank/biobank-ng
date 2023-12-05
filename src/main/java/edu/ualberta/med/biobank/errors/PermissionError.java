package edu.ualberta.med.biobank.errors;

public class PermissionError extends AppError {
    public PermissionError(String message) {
        super("permission: " + message);
    }
}
