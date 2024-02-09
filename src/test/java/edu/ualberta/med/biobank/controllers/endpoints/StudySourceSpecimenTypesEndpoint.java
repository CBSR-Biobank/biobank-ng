package edu.ualberta.med.biobank.controllers.endpoints;

public record StudySourceSpecimenTypesEndpoint(String nameshort) implements Endpoint {
    @Override
    public String url() {
        return "/studies/%s/source-specimen-types".formatted(nameshort);
    }
}
