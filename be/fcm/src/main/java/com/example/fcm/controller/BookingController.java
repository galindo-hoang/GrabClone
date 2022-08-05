package com.example.fcm.controller;

import com.example.fcm.model.dto.InformationBookingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.example.fcm.controller
 * Created by Admin
 * Date 8/4/2022 - 3:25 PM
 * Description: ...
 */

@RestController
@RequestMapping("/booking")
public class BookingController {
    @PostMapping("/")
    public ResponseEntity<InformationBookingDTO>sendInformationBookingToDriver(@RequestBody InformationBookingDTO informationBookingDTO){

        return ResponseEntity.ok(new InformationBookingDTO());
    }
}
