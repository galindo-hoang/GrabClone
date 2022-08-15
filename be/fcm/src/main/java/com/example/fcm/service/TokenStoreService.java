package com.example.fcm.service;

import com.example.fcm.model.entity.FcmTokenRecord;

public interface TokenStoreService {
    FcmTokenRecord findByUserId(Integer userId);

    FcmTokenRecord findByFcmToken(String fcmToken);
    
    void save(FcmTokenRecord fcmTokenRecord);
}
