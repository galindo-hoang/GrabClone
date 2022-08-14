package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverLocationDto {
    private Integer bookingId;
    private Integer userId;
}
