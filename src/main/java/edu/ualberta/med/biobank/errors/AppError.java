package edu.ualberta.med.biobank.errors;

public abstract class AppError {

    final private String message;

    public String getMessage() {
        return message;
    }

    AppError(String message) {
        this.message = message;
    }

}
