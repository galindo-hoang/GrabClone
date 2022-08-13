package com.example.booking.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RideRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer bookingId;

    @Column(nullable = false)
    private Integer driverId;

    @Column(nullable = false)
    private RideStatus status;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startTime;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date endTime;
}

enum RideStatus {
    ACCEPTED,
    STARTED,
    CANCELLED,
    COMPLETED
}