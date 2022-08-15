package com.example.booking.service;

import com.example.booking.model.entity.BookingRecord;
import com.example.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookingStoreService {
    @Autowired
    private BookingRepository bookingRepository;

    public BookingRecord saveBooking(BookingRecord bookingRecord) {
        return bookingRepository.save(bookingRecord);
    }
}
