package com.example.booking.repository;

import com.example.booking.model.entity.DriverRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<DriverRecord, Integer> {
    DriverRecord findByDriverUsername(String driverUsername);
}
