package com.example.booking.controller;

import com.example.booking.service.BookingStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.model.dto.BookingDto;
import com.example.booking.model.entity.BookingRecord;

import com.example.booking.service.BookingService;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    private BookingStoreService bookingService;
    
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        try {
            BookingRecord bookingRecord = new BookingRecord(bookingDto.getUserId(), bookingDto.getRoomId(), bookingDto.getCheckInDate(), bookingDto.getCheckOutDate());

            BookingRecord bookingRecordSaving = bookingService.saveBooking(bookingRecord);
            return ResponseEntity.ok(new BookingDto(bookingRecord));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bookingDto);
    }
}
