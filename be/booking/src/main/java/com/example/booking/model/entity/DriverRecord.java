package com.example.booking.model.entity;

import com.example.clients.feign.DriverLocation.MapCoordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DriverRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String driverUsername;

    @Column
    private String phoneNumber;

    @Embedded
    private MapCoordinate location;

    @Column
    private String geoHash;

    @Column
    private Float starEvaluate;

    @Column
    private Integer countEvaluate;

    @Column
    private Integer countRide;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date locationUpdateTime;

}
