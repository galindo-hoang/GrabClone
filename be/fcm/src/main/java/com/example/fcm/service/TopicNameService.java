package com.example.fcm.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.fcm.model.entity.TopicNameRecord;
import com.example.fcm.repository.TopicNameRepository;
import com.example.fcm.service.TopicNameService;

@Service
public class TopicNameService {
    @Autowired
    private TopicNameRepository topicNameRepository;

    public TopicNameRecord findByTopicName(String topicName) {
        return topicNameRepository.findByTopicName(topicName);
    }

    public TopicNameRecord save(TopicNameRecord topicNameRecord) {
        return topicNameRepository.save(topicNameRecord);
    }
}
