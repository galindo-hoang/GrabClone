package com.example.clients.feign.logger;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Logger {
    private String requestId;
    private String uri;
    private String method;
    private String requestData;
    private String responseData;
    private String headerRequest;
    private String headerResponse;
    private int httpResult;
    private String requestIP;
    private String parameter;
    private int timeProcess;
}

