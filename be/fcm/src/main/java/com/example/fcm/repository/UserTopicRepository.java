package com.example.fcm.repository;

import com.example.fcm.model.entity.TopicNameRecord;
import com.example.fcm.model.entity.UserTopicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopicRecord, Integer> {
    UserTopicRecord findByUsernameAndTopicNameRecord(String username, TopicNameRecord topicNameRecord);
}
