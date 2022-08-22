package com.example.booking.service;

import com.example.booking.model.dto.BookingResponseDto;
import com.example.booking.utils.BookingNotFoundException;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
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
import javax.persistence.Query;
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

    @Cacheable(value = "BookingRecord")
    public List<BookingRecord> findAll() {
        return bookingRepository.findAll();
    }

    @Cacheable(value = "BookingRecord", key = "#id")
    public BookingRecord findById(Integer id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking Not Found"));
    }

    @Cacheable(value = "BookingRecord")
    public List<BookingResponseDto> findTopDeparture(String phoneNumber, Integer limit) {
        Query query = entityManager.createQuery("select b.pickupCoordinate.latitude as latitude,b.pickupCoordinate.longitude as longitude,count(b.id) as count " +
                "from BookingRecord b " +
                "where b.phonenumber = :phoneNumber " +
                "group by b.pickupCoordinate.latitude,b.pickupCoordinate.longitude " +
                "order by count(b.id) desc ").setMaxResults(limit);
        return (List<BookingResponseDto>) query
                .setParameter("phoneNumber", phoneNumber)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] objects, String[] strings) {
                                return new BookingResponseDto(((Double) objects[0]).doubleValue(),
                                        ((Double) objects[1]).doubleValue(),
                                        ((Long) objects[2]).intValue());
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                )
                .getResultList();
    }

    @Cacheable(value = "BookingRecord")
    public List<BookingResponseDto> findTopDestination(String phoneNumber, Integer limit) {
        Query query = entityManager.createQuery("select b.dropoffCoordinate.latitude as latitude,b.dropoffCoordinate.longitude as longitude,count(b.id) as count " +
                "from BookingRecord b " +
                "where b.phonenumber = :phoneNumber " +
                "group by b.dropoffCoordinate.latitude,b.dropoffCoordinate.longitude " +
                "order by count(b.id) desc ").setMaxResults(limit);
        return (List<BookingResponseDto>) query
                .setParameter("phoneNumber", phoneNumber)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] objects, String[] strings) {
                                return new BookingResponseDto(((Double) objects[0]).doubleValue(),
                                        ((Double) objects[1]).doubleValue(),
                                        ((Long) objects[2]).intValue());
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                )
                .getResultList();
    }
    @CacheEvict(value = "BookingRecord", key = "#id")
    public void removeBooking(Integer id) {
        BookingRecord bookingRecord = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking Not Found"));
        bookingRepository.delete(bookingRecord);
    }

    @CachePut(value = "BookingRecord", key = "#id")
    public BookingRecord updateBooking(Integer id, BookingRecord bookingRecord) {
        BookingRecord bookingRecordFindById = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking Not Found"));
        //ToDo set new properties
        bookingRecordFindById.setCreatedAt(new Date());

        return bookingRepository.save(bookingRecordFindById);
    }
}
