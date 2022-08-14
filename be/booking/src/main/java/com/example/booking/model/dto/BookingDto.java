package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private String phonenumber;
    private Double latitude;
    private Double longtitude;
    private String typeCar;
    private String state;
    private String paymentMethod;
    private Float price;
}
