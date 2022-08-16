package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAcceptanceDto {
    private Integer userId;
    private Integer bookingId;
}
