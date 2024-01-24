package edu.ualberta.med.biobank.controllers.endpoints;

public record PatientCommentsListEndpoint(String pnumber) implements Endpoint {
    @Override
    public String url() {
        return "/patients/%s/comments".formatted(pnumber);
    }
}
