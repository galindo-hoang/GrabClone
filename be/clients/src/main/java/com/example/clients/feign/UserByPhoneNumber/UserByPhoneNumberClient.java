package com.example.clients.feign.UserByPhoneNumber;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("authentication")
@Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVjIiwicm9sZXMiOlsiUk9MRV9EUklWRVIiXSwiaXNzIjoiaHR0cDovL2hvc3QuZG9ja2VyLmludGVybmFsOjgwODEvbG9naW4iLCJleHAiOjE2NjA0MDY0MDF9.RZ0MdJTeDFCKzDhw-dmzmmZM4wKjE3fZ1JgCx0jSKuc")
public interface UserByPhoneNumberClient {
    @GetMapping(path = "/api/v1/users/{phoneNumber}")
    UserByPhoneNumber getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber);
}
