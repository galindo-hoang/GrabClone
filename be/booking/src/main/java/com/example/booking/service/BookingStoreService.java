package com.example.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking.model.entity.BookingRecord;
import com.example.booking.repository.BookingRepository;

@Service
@Transactional
public class BookingStoreService {
    @Autowired
    private BookingRepository bookingRepository;

    public BookingRecord save(BookingRecord bookingRecord) {
        return bookingRepository.save(bookingRecord);
    }

    public BookingRecord findById(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public List<BookingRecord> findByUserIdAndPhoneNumber(Integer userId, String phoneNumber) {
        return bookingRepository.findByUserIdAndPhoneNumber(userId, phoneNumber);
    }
}
