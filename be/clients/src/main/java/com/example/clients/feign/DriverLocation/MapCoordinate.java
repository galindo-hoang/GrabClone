package com.example.clients.feign.DriverLocation;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapCoordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double latitude;
    private Double longitude;
}
