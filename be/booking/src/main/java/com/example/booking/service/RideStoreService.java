package com.example.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking.model.entity.RideRecord;
import com.example.booking.repository.RideRepository;

@Service
@Transactional
public class RideStoreService {
    @Autowired
    private RideRepository rideRepository;

    public RideRecord save(RideRecord rideRecord) {
        return rideRepository.save(rideRecord);
    }
}
