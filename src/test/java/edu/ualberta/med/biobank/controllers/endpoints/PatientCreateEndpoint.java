package edu.ualberta.med.biobank.controllers.endpoints;

public record PatientCreateEndpoint() implements Endpoint {
    @Override
    public String url() {
        return "/patients";
    }
}
