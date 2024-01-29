package edu.ualberta.med.biobank.errors;

public class Forbidden extends AppError {

    private static final long serialVersionUID = 1L;

    public Forbidden(String message) {
        super(message);
    }
}
