package com.example.booking.controller;

import com.example.booking.model.dto.BookingAcceptanceDto;
import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.entity.*;
import com.example.booking.service.BookingStoreService;
import com.example.clients.feign.UserByPhoneNumber.UserByPhoneNumber;
import com.example.clients.feign.UserByPhoneNumber.UserByPhoneNumberClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    private BookingStoreService bookingService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/create_booking")
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        try {
            // get user by phonenumber
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVjIiwicm9sZXMiOlsiUk9MRV9EUklWRVIiXSwiaXNzIjoiaHR0cDovL2hvc3QuZG9ja2VyLmludGVybmFsOjgwODEvbG9naW4iLCJleHAiOjE2NjA0MDc2NzV9.C7hcOWM6Em5vz7g9F0fk28EAiPexR3mhE7Re5waXi0k");
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8081/api/v1/users/{phonenumber}", HttpMethod.GET, requestEntity,
                    Integer.class, bookingDto.getPhonenumber());
            System.out.println(response.getBody());
            BookingRecord bookingRecord = BookingRecord.builder()
                     .userId(response.getBody())
                    .phonenumber(bookingDto.getPhonenumber())
                    .coordinates(new BookingCoordinate(bookingDto.getLatitude(), bookingDto.getLongtitude()))
                    .typeCar(TypeCar.valueOf(bookingDto.getTypeCar()))
                    .state(StateBooking.CREATED)
                    .paymentMethod(PaymentMethod.valueOf(bookingDto.getPaymentMethod()))
                    .price(bookingDto.getPrice())
                    .createdAt(new Date())
                    .build();
            bookingService.saveBooking(bookingRecord);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accept_booking")
    public ResponseEntity<BookingAcceptanceDto> acceptBooking(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
        return ResponseEntity.badRequest().build();
    }

    // @PostMapping("/update_driver_location")
    // public ResponseEntity<BookingAcceptanceDto> updateDriverLocation(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

    // @PostMapping("/finish_ride")
    // public ResponseEntity<BookingAcceptanceDto> finishRide(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }545

    // @PostMapping("/cancel_booking")
    // public ResponseEntity<BookingAcceptanceDto> cancelBooking(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

    // @PostMapping("/cancel_ride")
    // public ResponseEntity<BookingAcceptanceDto> cancelRide(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

}
