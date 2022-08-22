package com.example.booking.service;

import com.example.booking.utils.BookingNotFoundException;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking.model.entity.BookingRecord;
import com.example.booking.repository.BookingRepository;

import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BookingStoreService {
    @Autowired
    private BookingRepository bookingRepository;
    @PersistenceContext
    private javax.persistence.EntityManager entityManager;
    public BookingRecord save(BookingRecord bookingRecord) {
        return bookingRepository.save(bookingRecord);
    }

    @Cacheable(value="BookingRecord")
    public List<BookingRecord> findAll() {
        return bookingRepository.findAll();
    }

    @Cacheable(value="BookingRecord", key="#id")
    public BookingRecord findById(Integer id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking Not Found"));
    }

    @Cacheable(value = "BookingRecord", key = "#page")
    public List<BookingRecord> findPages(Integer page) {
        return bookingRepository.findAll(PageRequest.of(page,5)).getContent();
    }
    public List<BookingRecord> findTopDeparture(String phoneNumber, Integer limit) {
           JPAQuery<BookingRecord> query = new JPAQuery<>(entityManager);
        return bookingRepository.findAll(PageRequest.of(0,limit)).getContent();
    }

    @CacheEvict(value="BookingRecord", key="#id")
    public void removeBooking(Integer id) {
        BookingRecord bookingRecord=bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking Not Found"));
        bookingRepository.delete(bookingRecord);
    }

    @CachePut(value="BookingRecord", key="#id")
    public BookingRecord updateBooking(Integer id, BookingRecord bookingRecord) {
        BookingRecord bookingRecordFindById=bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking Not Found"));
        //ToDo set new properties
        bookingRecordFindById.setCreatedAt(new Date());

        return bookingRepository.save(bookingRecordFindById);
    }
}
