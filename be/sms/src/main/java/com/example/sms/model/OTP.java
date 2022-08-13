package com.example.sms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTP implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private CompositeKeyOTP compositeKeyOTP;
    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public OTP(String phonenumber, String otp) {
        this.compositeKeyOTP = new CompositeKeyOTP(phonenumber, otp);
        this.createdAt = new Date();
    }
}

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
class CompositeKeyOTP {
    private String phoneNumber;
    private String otp;
}