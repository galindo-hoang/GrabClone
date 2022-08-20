package com.example.booking.model.domain;

import lombok.*;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapCoordinate {
    private Double latitude;
    private Double longitude;
}
