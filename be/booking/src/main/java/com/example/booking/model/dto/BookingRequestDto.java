package com.example.booking.model.dto;

import com.example.booking.model.domain.MapCoordinate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {
    private String phonenumber;
    private MapCoordinate pickupLocation;
    private MapCoordinate dropoffLocation;
    private String typeCar;
    private String state;
    private String paymentMethod;
    private Float price;
    private String username;
}


