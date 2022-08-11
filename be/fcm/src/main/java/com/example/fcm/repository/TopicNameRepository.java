package com.example.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fcm.model.entity.TopicNameRecord;

@Repository
public interface TopicNameRepository extends JpaRepository<TopicNameRecord, Integer> {
    TopicNameRecord findByTopicName(String topicName);
}
