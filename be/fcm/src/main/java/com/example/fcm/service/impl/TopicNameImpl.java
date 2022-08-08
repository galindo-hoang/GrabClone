package com.example.fcm.service.impl;

import org.springframework.stereotype.Service;

import com.example.fcm.model.entity.TopicNameRecord;
import com.example.fcm.repository.TopicNameRepository;
import com.example.fcm.service.TopicNameService;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TopicNameImpl implements TopicNameService {
    @Autowired
    private TopicNameRepository topicNameRepository;
    
    @Override
    public TopicNameRecord findByTopicName(String topicName) {
        return topicNameRepository.findByTopicName(topicName);
    }
    
    @Override
    public void save(TopicNameRecord topicNameRecord) {
        topicNameRepository.save(topicNameRecord);
    }
}