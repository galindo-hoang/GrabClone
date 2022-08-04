package com.example.fcm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * com.example.fcm.model.entity
 * Created by Admin
 * Date 8/2/2022 - 10:22 PM
 * Description: ...
 */
@Entity
@Table(schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,name = "phonenumber")
    private String phoneNumber;


    @ManyToOne
    @JoinColumn(name = "departure")
    private Address departure;


    @ManyToOne
    @JoinColumn(name = "destination")
    private Address destination;

    @Column(nullable = false,name="typecar")
    private String typeCar;


    @Column(nullable = false,name="customerId")
    private Integer customerId;


    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="booktime",nullable = false)
    private Date bookTime;


    @Column(name = "status",nullable = false)
    private status status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id",referencedColumnName = "id")
    private Trip trip;

    @Version
    private Integer version;

    public enum status {
        CANCELLED,PENDING,APPROVED
    }




}
