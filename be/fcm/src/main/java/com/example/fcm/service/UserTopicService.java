package com.example.fcm.service;

import com.example.fcm.model.entity.TopicNameRecord;
import com.example.fcm.model.entity.UserTopicRecord;
import com.example.fcm.repository.UserTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTopicService {
    @Autowired
    private UserTopicRepository userTopicRepository;
    public UserTopicRecord save(UserTopicRecord userTopicRecord) {
        return userTopicRepository.save(userTopicRecord);
    }

    public UserTopicRecord findByUsernameAndTopicNameRecord(String username, TopicNameRecord topicNameRecord) {
        return userTopicRepository.findByUsernameAndTopicNameRecord(username, topicNameRecord);
    }
}
