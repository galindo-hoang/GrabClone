package com.example.fcm.service.impl;

import org.springframework.stereotype.Service;

import com.example.fcm.model.entity.FcmTokenRecord;
import com.example.fcm.repository.FCMTokenRepository;
import com.example.fcm.service.TokenStoreService;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TokenStoreImpl implements TokenStoreService {
    @Autowired
    private FCMTokenRepository fcmTokenRepository;

    @Override
    public FcmTokenRecord findByUserId(Integer userId) {
        return fcmTokenRepository.findByUserId(userId);
    }

    @Override
    public FcmTokenRecord findByFcmToken(String fcmToken) {
        return fcmTokenRepository.findByFcmToken(fcmToken);
    }

    @Override
    public void save(FcmTokenRecord fcmTokenRecord) {
        fcmTokenRepository.save(fcmTokenRecord);
    }
}
