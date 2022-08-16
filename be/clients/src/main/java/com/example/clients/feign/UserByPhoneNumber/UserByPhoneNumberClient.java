package com.example.clients.feign.UserByPhoneNumber;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("AUTHENTICATION")
public interface UserByPhoneNumberClient {
    @GetMapping(path = "api/v1/users/{phoneNumber}")
    ResponseEntity<Integer> getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber);
}
