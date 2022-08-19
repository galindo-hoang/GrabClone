package com.example.booking.model.dto;

import com.example.booking.model.domain.MapCoordinate;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDto {
    private String username;
    private MapCoordinate location;
}
