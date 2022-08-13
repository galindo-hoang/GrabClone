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
@Builder
public class BookingRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String phonenumber;
    @Embedded
    private BookCoordinates coordinates;
    @Column
    private TypeCar typeCar;
    @Column
    private StateBooking state;
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
    private Integer userId;

}

@Embeddable
@Getter
@Setter
class BookCoordinates {
    private Double latitude;
    private Double longitude;

}

enum StateBooking {
    PENDING,
    ACCEPTED,
    REJECTED
}

enum TypeCar {
    MOTORCYCLE,
    CAR
}

enum PaymentMethod {
    CASH,
    CREDIT_CARD
}