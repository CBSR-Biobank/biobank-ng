package edu.ualberta.med.biobank.errors;

public class BadRequest extends AppError {

    private static final long serialVersionUID = 1L;

    public BadRequest(String message) {
        super(message);
    }
}
