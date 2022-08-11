package com.example.fcm.model.entity;

import lombok.*;
import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicNameRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String topicName;
}
