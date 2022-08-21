package com.example.fcm.model.entity;

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
@Builder
public class TopicNameRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String topicName;
}
