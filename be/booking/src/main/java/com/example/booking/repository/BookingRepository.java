package com.example.booking.repository;


import com.example.booking.model.entity.BookingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface BookingRepository extends JpaRepository<BookingRecord, Integer> {

}
