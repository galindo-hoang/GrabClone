package com.example.booking.model.dto;

import lombok.*;
import java.util.List;

import com.example.booking.model.domain.MapCoordinate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopCoorRespDto {
    List<MapCoordinate> topCoordinates;
}
