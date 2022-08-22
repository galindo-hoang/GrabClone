package com.example.booking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.booking.model.entity.BookingLocation;

@Repository
public interface LocationRepository extends CrudRepository<BookingLocation, String> {

}