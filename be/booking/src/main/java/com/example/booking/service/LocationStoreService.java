/*
package com.example.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking.model.entity.BookingLocation;

@Service
@Transactional
public class LocationStoreService {
    @Autowired
    private RedisTemplate<String, BookingLocation> redisTemplate;

    public void save(BookingLocation bookingLocation) {
        redisTemplate.opsForValue().set(bookingLocation.getId(), bookingLocation);
    }

    public BookingLocation findById(String id) {
        return redisTemplate.opsForValue().get(id);
    }
}
*/
