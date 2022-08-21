package com.example.clients.feign.DriverLocation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("booking")
public interface DriverLocationClients {
    @PostMapping(path = "api/v1/booking/update_location")
    ResponseEntity<DriverLocationDto> updateLocation(@RequestBody DriverLocationDto driverLocationDto);
}
