package edu.ualberta.med.biobank.controllers.endpoints;

public record CeventCommentsListEndpoint(String pnumber, Integer vnumber) implements Endpoint {
    @Override
    public String url() {
        return "/patients/%s/collection-events/%d/comments".formatted(pnumber, vnumber);
    }
}
