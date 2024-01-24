package edu.ualberta.med.biobank.controllers.endpoints;

public record PatientAddEndpoint() implements Endpoint {
    @Override
    public String url() {
        return "/patients";
    }
}
