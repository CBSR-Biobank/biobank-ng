package edu.ualberta.med.biobank.controllers.endpoints;

public record CollectionEventEndpoint(String pnumber) implements Endpoint {
    @Override
    public String url() {
        return "/patients/%s/collection-events".formatted(pnumber);
    }
}
