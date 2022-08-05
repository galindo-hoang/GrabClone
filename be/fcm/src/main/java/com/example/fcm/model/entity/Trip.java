package com.example.fcm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * com.example.fcm.model.entity
 * Created by Admin
 * Date 8/4/2022 - 12:56 PM
 * Description: ...
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(schema = "public")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,name = "driverid")
    private InternalError driverId;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeDeparture;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeDestination;

    @OneToOne(mappedBy = "trip")
    private Booking booking;


    @Version
    private Integer version;

}
