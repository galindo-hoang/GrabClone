package com.example.booking.model.entity;

import lombok.*;
import javax.persistence.*;

import org.springframework.data.redis.core.RedisHash;

import com.example.booking.model.domain.LocationCount;

import java.io.Serializable;
import java.util.List;

@RedisHash("booking_location")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingLocation implements Serializable {
    @Id
    private String id;

    @Embedded
    private List<LocationCount> locationCounts;
}
