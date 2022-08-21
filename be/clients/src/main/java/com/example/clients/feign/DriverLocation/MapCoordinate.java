package com.example.clients.feign.DriverLocation;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapCoordinate {
    private Double latitude;
    private Double longitude;
}
