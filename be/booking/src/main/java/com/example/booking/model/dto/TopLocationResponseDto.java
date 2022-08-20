package com.example.booking.model.dto;

import lombok.*;
import java.util.List;

import com.example.booking.model.domain.LocationCount;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopLocationResponseDto {
    private List<LocationCount> topLocations;
}
