package edu.ualberta.med.biobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotActiveException extends RuntimeException {

    public UserNotActiveException(String exception) {
        super(exception);
    }

}
