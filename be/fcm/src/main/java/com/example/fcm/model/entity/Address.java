package com.example.fcm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * com.example.fcm.model.entity
 * Created by Admin
 * Date 8/4/2022 - 12:39 PM
 * Description: ...
 */

@Entity
@Table(schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    @Version
    private Integer version;


    @OneToMany(mappedBy = "departure")
    private List<Booking>  departureList;

    @OneToMany(mappedBy = "destination")
    private List<Booking> destinationList;
}
