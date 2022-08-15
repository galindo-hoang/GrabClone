package com.example.booking.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.booking.model.domain.MapCoordinate;
import com.example.booking.model.domain.PaymentMethod;
import com.example.booking.model.domain.BookingState;
import com.example.booking.model.domain.TypeCar;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String phonenumber;

    @Embedded
    @Column(name = "pickUpCoordinate")
    private MapCoordinate pickupCoordinate;
    @Embedded
    @Column(name="dropOffCoordinate")
    private MapCoordinate dropoffCoordinate;
    @Column
    private TypeCar typeCar;
    @Column
    private BookingState state;
    @Column
    private PaymentMethod paymentMethod;
    @Column
    private Float price;
    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdAt;
    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date updatedAt;
    @Column
    private Integer passengerId;
}
