package edu.ualberta.med.biobank.exception;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.errors.Forbidden;
import edu.ualberta.med.biobank.errors.PermissionError;
import edu.ualberta.med.biobank.errors.Unauthorized;
import edu.ualberta.med.biobank.errors.ValidationError;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            exception.getMessage(),
            request.getDescription(false)
        );

        logger.error("an exception occurred", exception);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppErrorException.class)
    public final ResponseEntity<ExceptionResponse> handleAppErrorException(
        AppErrorException appErrorException,
        WebRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (appErrorException.appError instanceof EntityNotFound) {
            status = HttpStatus.NOT_FOUND;
        }

        if (appErrorException.appError instanceof Forbidden || appErrorException.appError instanceof PermissionError) {
            status = HttpStatus.FORBIDDEN;
        }

        if (appErrorException.appError instanceof Unauthorized) {
            status = HttpStatus.UNAUTHORIZED;
        }

        if (appErrorException.appError instanceof ValidationError) {
            status = HttpStatus.BAD_REQUEST;
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            appErrorException.appError.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(exceptionResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
	HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            "Required request body is missing or malformed",
            request.getDescription(false)
        );

        return new ResponseEntity<>(exceptionResponse, status);

        //return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }
}
