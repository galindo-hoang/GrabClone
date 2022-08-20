package com.example.booking.model.domain;

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
