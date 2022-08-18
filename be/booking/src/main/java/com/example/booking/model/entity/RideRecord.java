package com.example.booking.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.booking.model.domain.RideState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RideRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer bookingId;

    @Column(nullable = false)
    private String driverUsername;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startTime;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endTime;

    @Enumerated(EnumType.STRING)
    private RideState rideState;
}