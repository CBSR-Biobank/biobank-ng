package edu.ualberta.med.biobank.controllers.endpoints;

import java.util.ArrayList;
import java.util.List;

public record StudyAnnotationTypesEndpoint(String nameshort, String ...status) implements Endpoint {
    private static final String baseUrl = "/studies/%s/annotation-types";

    @Override
    public String url() {
        String url = baseUrl.formatted(nameshort);
        if (status == null) {
            return url;
        }

        List<String> queryParams = new ArrayList<>();
        for (String s : status) {
            queryParams.add("status=%s".formatted(s));
        }

        return "%s?%s".formatted(url, String.join("&", queryParams));
    }

    public static class Builder {
        private String nameshort;
        private List<String> statuses;

        public Builder() {
            nameshort = "";
            statuses = new ArrayList<>();
        }

        public Builder withNameShort(String name) {
            nameshort = name;
            return this;
        }

        public Builder withStatus(String s) {
            statuses.add(s);
            return this;
        }

        public StudyAnnotationTypesEndpoint build() {
            if (nameshort.isBlank()) {
                throw new RuntimeException("nameshort is blank");
            }
            return new StudyAnnotationTypesEndpoint(nameshort, statuses.toArray(new String[0]));
        }
    }
}
