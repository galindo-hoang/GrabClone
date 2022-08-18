package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDto {
    private String username;
    private Double latitude;
    private Double longitude;
}
