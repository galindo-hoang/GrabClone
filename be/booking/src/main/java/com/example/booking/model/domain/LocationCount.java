package com.example.booking.model.domain;

import com.example.clients.feign.DriverLocation.MapCoordinate;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationCount {
    private MapCoordinate coordinate;
    private Integer count;
}
