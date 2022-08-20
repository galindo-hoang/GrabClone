package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booking.model.entity.BookingRecord;

@Repository
public interface BookingRepository extends JpaRepository<BookingRecord, Integer> {

}
