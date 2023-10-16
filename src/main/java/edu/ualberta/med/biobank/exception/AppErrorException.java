package edu.ualberta.med.biobank.exception;

import edu.ualberta.med.biobank.errors.AppError;

public class AppErrorException extends RuntimeException {

    public AppError appError;

    public AppErrorException(AppError appError) {
        super("application error");
        this.appError = appError;
    }

}
