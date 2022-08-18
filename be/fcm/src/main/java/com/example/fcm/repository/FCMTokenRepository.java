package com.example.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fcm.model.entity.FcmTokenRecord;

@Repository
public interface FCMTokenRepository extends JpaRepository<FcmTokenRecord, Integer> {
    FcmTokenRecord findByUsername(String username);

    FcmTokenRecord findByFcmToken(String fcmToken);
}
