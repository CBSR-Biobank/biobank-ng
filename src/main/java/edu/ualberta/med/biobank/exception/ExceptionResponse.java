package edu.ualberta.med.biobank.exception;

import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime timestamp, String message, String details) {}
