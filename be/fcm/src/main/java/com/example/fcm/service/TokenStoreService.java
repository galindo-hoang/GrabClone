package com.example.fcm.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.fcm.model.entity.FcmTokenRecord;
import com.example.fcm.repository.FCMTokenRepository;
import com.example.fcm.service.TokenStoreService;
import com.example.fcm.model.entity.FcmTokenRecord;

public class TokenStoreService {
    @Autowired
    private FCMTokenRepository fcmTokenRepository;

    public FcmTokenRecord findByUserId(Integer userId) {
        return fcmTokenRepository.findByUserId(userId);
    }

    public FcmTokenRecord findByFcmToken(String fcmToken) {
        return fcmTokenRepository.findByFcmToken(fcmToken);
    }
    
    public FcmTokenRecord save(FcmTokenRecord fcmTokenRecord) {
        return fcmTokenRepository.save(fcmTokenRecord);
    }
}
