package edu.ualberta.med.biobank.controllers.endpoints;

import java.util.ArrayList;
import java.util.List;

public record ClinicNamesEndpoint(String ...status) implements Endpoint {
    private static final String baseUrl = "/clinics/names";

    @Override
    public String url() {
        if (status == null) {
            return baseUrl;
        }

        List<String> queryParams = new ArrayList<>();
        for (String s : status) {
            queryParams.add("status=%s".formatted(s));
        }

        return "%s?%s".formatted(baseUrl, String.join("&", queryParams));
    }
}
