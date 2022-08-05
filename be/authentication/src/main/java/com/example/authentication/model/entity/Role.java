package com.example.authentication.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    @Version
    private Integer version;

    public Role(RoleName name) {
        this.name = name;
    }

    public enum RoleName {
        ROLE_ADMIN, ROLE_USER, ROLE_DRIVER, ROLE_TELEPHONIST;
    }
}
