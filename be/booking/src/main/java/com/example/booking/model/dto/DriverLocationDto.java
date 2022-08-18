package com.example.booking.model.dto;

import com.example.booking.model.domain.MapCoordinate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDto {
    private String username;
    private MapCoordinate location;
}
