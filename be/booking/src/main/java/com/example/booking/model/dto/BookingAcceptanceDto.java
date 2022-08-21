package com.example.booking.model.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAcceptanceDto {
    private String username;
    private Integer bookingId;
    private Date acceptanceDriverDateTime;
}
