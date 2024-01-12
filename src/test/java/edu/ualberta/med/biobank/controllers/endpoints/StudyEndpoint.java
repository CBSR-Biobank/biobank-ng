package edu.ualberta.med.biobank.controllers.endpoints;

public record StudyEndpoint(String nameShort) implements Endpoint {
    @Override
    public String url() {
        return "/studies/%s".formatted(nameShort);
    }
}
