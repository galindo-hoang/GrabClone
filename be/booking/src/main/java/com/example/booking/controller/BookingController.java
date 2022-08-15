package com.example.booking.controller;

import com.example.booking.model.dto.BookingAcceptanceDto;
import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.entity.*;
import com.example.booking.service.BookingStoreService;
import com.example.clients.feign.UserByPhoneNumber.UserByPhoneNumberClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingStoreService bookingService;
    private final UserByPhoneNumberClient userByPhoneNumberClient;

    @PostMapping("/create_booking")
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        try {
            // get user by phonenumber
            /*HttpHeaders headers = new HttpHeaders();
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8081/api/v1/users/{phonenumber}", HttpMethod.GET, requestEntity,
                    Integer.class, bookingDto.getPhonenumber());*/
            Integer userId = userByPhoneNumberClient.getUserByPhoneNumber(bookingDto.getPhonenumber()).getBody();
            BookingRecord bookingRecord = BookingRecord.builder()
                    .userId(userId)
                    .phonenumber(bookingDto.getPhonenumber())
                    .coordinates(new BookingCoordinate(bookingDto.getLatitude(), bookingDto.getLongtitude()))
                    .typeCar(TypeCar.valueOf(bookingDto.getTypeCar()))
                    .state(StateBooking.CREATED)
                    .paymentMethod(PaymentMethod.valueOf(bookingDto.getPaymentMethod()))
                    .price(bookingDto.getPrice())
                    .createdAt(new Date())
                    .build();
            bookingService.saveBooking(bookingRecord);
            return ResponseEntity.ok(bookingDto);
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
