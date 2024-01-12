package edu.ualberta.med.biobank.controllers.endpoints;

public record PatientNumberEndpoint(String pnumber) implements Endpoint {
    @Override
    public String url() {
        return "/patients/%s".formatted(pnumber);
    }
}
