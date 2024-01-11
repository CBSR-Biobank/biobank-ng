package edu.ualberta.med.biobank.errors;

public class EntityNotFound extends AppError {

    private static final long serialVersionUID = 1L;

    public EntityNotFound(String message) {
        super(message);
    }
}
