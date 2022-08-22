package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booking.model.entity.BookingRecord;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingRecord, Integer> {
}
