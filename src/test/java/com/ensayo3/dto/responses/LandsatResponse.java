package com.ensayo3.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandsatResponse {

    private String date;
    private String id;
    private Resource resource;
    private String service_version;
    private String url;

    @Data
    public static class Resource {
        private String dataset;
        private String planet;
    }
}
