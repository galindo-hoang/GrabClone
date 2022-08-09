package com.example.authentication.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String phonenumber;
    @Column(nullable = false, unique = true, length = 6)
    private String otp;
    @ManyToMany
    @JoinTable(
            name = "User_Role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles = new ArrayList<>();

    @Version
    private Integer version;

    public User(String username, String password, String phonenumber,String otp) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
        this.otp = otp;
    }
}
