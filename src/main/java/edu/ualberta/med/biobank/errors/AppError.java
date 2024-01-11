package edu.ualberta.med.biobank.errors;

import java.io.Serializable;

public abstract class AppError implements Serializable {

    private static final long serialVersionUID = 1L;

    final private String message;

    public String getMessage() {
        return message;
    }

    AppError(String message) {
        this.message = message;
    }

}
