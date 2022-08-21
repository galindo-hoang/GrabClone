package com.example.booking.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    @JsonProperty("duration")

    private String duration;
    @JsonProperty("distance")
    private String distance;
    @Override
    public String toString() {
        return "Route [duration=" + duration + ", distance=" + distance + "]";
    }
}

