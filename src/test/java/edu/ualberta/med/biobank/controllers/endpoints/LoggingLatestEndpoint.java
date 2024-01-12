package edu.ualberta.med.biobank.controllers.endpoints;

public record LoggingLatestEndpoint() implements Endpoint {
    @Override
    public String url() {
        return "/logging/latest";
    }
}
