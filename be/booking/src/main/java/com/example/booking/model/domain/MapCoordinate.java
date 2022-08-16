package com.example.booking.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapCoordinate {
    @Column(name = "latitude",insertable = false,updatable = false)
    private Double latitude;
    @Column(name = "longitude",insertable = false,updatable = false)
    private Double longitude;
}
