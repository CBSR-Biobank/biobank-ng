package edu.ualberta.med.biobank.exception;

import edu.ualberta.med.biobank.errors.AppError;

public class AppErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppError appError;

    public AppErrorException(AppError appError) {
        super("application error");
        this.appError = appError;
    }

}
