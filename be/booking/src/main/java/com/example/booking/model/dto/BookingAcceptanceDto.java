package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAcceptanceDto {
    private String username;
    private Integer bookingId;
}
