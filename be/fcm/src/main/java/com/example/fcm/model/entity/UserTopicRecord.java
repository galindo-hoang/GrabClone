package com.example.fcm.model.entity;

import com.example.fcm.model.domain.TopicState;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class UserTopicRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicNameRecord topicNameRecord;

    @Column(nullable = false)
    private String username;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TopicState topicState;
}

