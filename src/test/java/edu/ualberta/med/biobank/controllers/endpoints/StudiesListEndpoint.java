package edu.ualberta.med.biobank.controllers.endpoints;

public record StudiesListEndpoint() implements Endpoint {
    @Override
    public String url() {
        return "/studies";
    }
}
