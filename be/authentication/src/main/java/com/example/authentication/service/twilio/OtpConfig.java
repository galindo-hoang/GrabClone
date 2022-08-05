package com.example.authentication.service.twilio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
public class OtpConfig {
    private String accountSid;
    private String authToken;
    private String trialNumber;
}
