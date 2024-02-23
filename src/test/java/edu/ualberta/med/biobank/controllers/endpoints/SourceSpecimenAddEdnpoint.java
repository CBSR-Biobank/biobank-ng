package edu.ualberta.med.biobank.controllers.endpoints;

public record SourceSpecimenAddEdnpoint() implements Endpoint {
    @Override
    public String url() {
        return "/specimens".formatted();
    }
}
