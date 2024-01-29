package edu.ualberta.med.biobank.controllers.endpoints;

public record PatientUpdateEndpoint(String pnumber) implements Endpoint {
    @Override
    public String url() {
        return "/patients/%s".formatted(pnumber);
    }
}
