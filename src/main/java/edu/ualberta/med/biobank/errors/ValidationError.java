package edu.ualberta.med.biobank.errors;

public class ValidationError extends AppError {

    private static final long serialVersionUID = 1L;

    public ValidationError(String message) {
        super(message);
    }
}
