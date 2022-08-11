package com.example.fcm.service;

import com.example.fcm.model.entity.TopicNameRecord;

public interface TopicNameService {
    TopicNameRecord findByTopicName(String topicName);

    void save(TopicNameRecord topicNameRecord);
}
