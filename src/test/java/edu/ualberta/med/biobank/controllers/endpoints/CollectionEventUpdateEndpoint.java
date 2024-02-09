package edu.ualberta.med.biobank.controllers.endpoints;

public record CollectionEventUpdateEndpoint(String pnumber, Integer vnumber) implements Endpoint {
    @Override
    public String url() {
        return "/patients/%s/collection-events/%d".formatted(pnumber, vnumber);
    }
}
