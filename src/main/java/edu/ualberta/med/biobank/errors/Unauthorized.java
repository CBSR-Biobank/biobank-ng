package edu.ualberta.med.biobank.errors;

public class Unauthorized extends AppError {

    private static final long serialVersionUID = 1L;

    public Unauthorized(String message) {
        super(message);
    }
}
