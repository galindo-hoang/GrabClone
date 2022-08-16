package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {
    private String phonenumber;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private String typeCar;
    private String state;
    private String paymentMethod;
    private Float price;
    private Integer userId;
}
