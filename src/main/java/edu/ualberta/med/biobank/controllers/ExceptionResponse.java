package edu.ualberta.med.biobank.controllers;

import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime timestamp, String message, String details) {}
