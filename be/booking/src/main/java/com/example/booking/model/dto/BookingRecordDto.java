package com.example.booking.model.dto;

import com.example.clients.feign.DriverLocation.MapCoordinate;
import com.example.booking.model.domain.PaymentMethod;
import com.example.booking.model.domain.TypeCar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRecordDto {
    private Integer bookingId;
    private MapCoordinate pickupLocation;
    private MapCoordinate dropoffLocation;
    private TypeCar typeCar;
    private float price;
    private PaymentMethod paymentMethod;
    private Date createdAt;
    private Integer rideId;
    private Date startTime;
}
