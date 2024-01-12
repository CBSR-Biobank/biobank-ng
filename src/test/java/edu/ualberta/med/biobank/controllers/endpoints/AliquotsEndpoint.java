package edu.ualberta.med.biobank.controllers.endpoints;

public record AliquotsEndpoint(String inventoryId) implements Endpoint {
    @Override
    public String url() {
        return "/specimens/%s/aliquots".formatted(inventoryId);
    }
}
