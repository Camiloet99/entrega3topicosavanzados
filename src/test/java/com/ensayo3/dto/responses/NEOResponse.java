package com.ensayo3.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NEOResponse {

    private Links links;
    private Integer element_count;
    private Map<String, List<NEO>> near_earth_objects;

    @Data
    public static class Links {
        private String next;
        private String previous;
        private String self;
    }

    @Data
    public static class NEO {
        private Links links;
        private String id;
        private String neo_reference_id;
        private String name;
        private String nasa_jpl_url;
        private Double absolute_magnitude_h;
        private EstimatedDiameter estimated_diameter;
        private Boolean is_potentially_hazardous_asteroid;
        private List<CloseApproachData> close_approach_data;
        private Boolean is_sentry_object;

        @Data
        public static class EstimatedDiameter {
            private Diameter kilometers;
            private Diameter meters;
            private Diameter miles;
            private Diameter feet;

            @Data
            public static class Diameter {
                private Double estimated_diameter_min;
                private Double estimated_diameter_max;
            }
        }

        @Data
        public static class CloseApproachData {
            private String close_approach_date;
            private String close_approach_date_full;
            private Long epoch_date_close_approach;
            private RelativeVelocity relative_velocity;
            private MissDistance miss_distance;
            private String orbiting_body;

            @Data
            public static class RelativeVelocity {
                private String kilometers_per_second;
                private String kilometers_per_hour;
                private String miles_per_hour;
            }

            @Data
            public static class MissDistance {
                private String astronomical;
                private String lunar;
                private String kilometers;
                private String miles;
            }
        }
    }
}

