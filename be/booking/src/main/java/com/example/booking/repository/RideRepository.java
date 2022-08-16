package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booking.model.entity.RideRecord;

@Repository
public interface RideRepository extends JpaRepository<RideRecord, Integer>  {
    
}
