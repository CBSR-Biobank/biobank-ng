package edu.ualberta.med.biobank.errors;

public class PermissionError extends AppError {

    private static final long serialVersionUID = 1L;

    public PermissionError(String message) {
        super("permission: " + message);
    }
}
