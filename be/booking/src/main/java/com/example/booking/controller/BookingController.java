package com.example.booking.controller;

import com.example.booking.model.domain.*;
import com.example.booking.model.dto.*;
import com.example.booking.model.entity.*;
import com.example.booking.service.*;

import java.util.Date;
import java.util.HashMap;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    private BookingStoreService bookingService;
    @Autowired
    private RideStoreService rideService;
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders requestHeader;
    private HttpEntity<String> requestEntity;

    // The key is the booking id
    private HashMap<Integer, BookingRecord> bookingRecordMap;
    // The key is the driver id
    private HashMap<Integer, Pair<RideRecord, BookingRecord>> rideRecordMap;

    public BookingController() {
        requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        requestHeader.set("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVjIiwicm9sZXMiOlsiUk9MRV9EUklWRVIiXSwiaXNzIjoiaHR0cDovL2hvc3QuZG9ja2VyLmludGVybmFsOjgwODEvbG9naW4iLCJleHAiOjE2NjA0MDc2NzV9.C7hcOWM6Em5vz7g9F0fk28EAiPexR3mhE7Re5waXi0k");
        requestEntity = new HttpEntity<String>(null, requestHeader);
        bookingRecordMap = new HashMap<>();
        rideRecordMap = new HashMap<>();
    }

    @PostMapping("/create_booking")
    public ResponseEntity<BookingRequestDto> createBooking(@RequestBody BookingRequestDto bookingDto) {
        try {
            // Create booking record and save it to database
            BookingRecord bookingRecord = BookingRecord.builder()
                    .passengerId(bookingDto.getUserId())
                    .phonenumber(bookingDto.getPhonenumber())
                    .pickupCoordinate(new MapCoordinate(bookingDto.getPickupLatitude(), bookingDto.getPickupLongitude()))
                    .dropoffCoordinate(new MapCoordinate(bookingDto.getDropoffLatitude(), bookingDto.getDropoffLongitude()))
                    .typeCar(TypeCar.valueOf(bookingDto.getTypeCar()))
                    .state(BookingStatus.CREATED)
                    .paymentMethod(PaymentMethod.valueOf(bookingDto.getPaymentMethod()))
                    .price(bookingDto.getPrice())
                    .createdAt(new Date())
                    .build();
            final BookingRecord savedBookingRecord = bookingService.save(bookingRecord);
            bookingRecordMap.put(savedBookingRecord.getId(), savedBookingRecord);


            // Send booking notification to all drivers using FCM service
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", "booking");
            jsonObject.put("title", "New booking");
            jsonObject.put("body", "A new booking is waiting for you");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", savedBookingRecord.toString());
                put("pickupLatitude", bookingDto.getPickupLatitude().toString());
                put("pickupLongitude", bookingDto.getPickupLongitude().toString());
                put("dropoffLatitude", bookingDto.getDropoffLatitude().toString());
                put("dropoffLongitude", bookingDto.getDropoffLongitude().toString());
                put("typeCar", bookingDto.getTypeCar());
                put("price", bookingDto.getPrice().toString());
                put("paymentMethod", bookingDto.getPaymentMethod());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8082/fcm/topic", HttpMethod.POST, requestEntity, Integer.class);
            requestEntity = new HttpEntity<String>(null, requestHeader);


            // Return success response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accept_booking")
    public ResponseEntity<BookingAcceptanceDto> acceptBooking(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
        try {
            // Return not found if booking record not found
            if (!bookingRecordMap.containsKey(bookingAcceptanceDto.getBookingId())) {
                return ResponseEntity.notFound().build();
            }


            // Unsubscribe the accepted driver from the booking topic
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", bookingAcceptanceDto.getUserId());
            jsonObject.put("topic", "booking");

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8082/fcm/unsubscribe", HttpMethod.POST, requestEntity, Integer.class);


            // Send booking already accepted notification to all drivers using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", "booking");
            jsonObject.put("title", "Booking claimed");
            jsonObject.put("body", "The booking has been claimed by another driver");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingAcceptanceDto.getBookingId().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/topic", HttpMethod.POST, requestEntity, Integer.class);


            // Update booking status to FINISHED and save it to database
            BookingRecord bookingRecord = bookingRecordMap.remove(bookingAcceptanceDto.getBookingId());
            bookingRecord.setState(BookingStatus.FINISHED);
            bookingService.save(bookingRecord);
            

            // Create ride record and save it to database
            RideRecord rideRecord = RideRecord.builder()
                    .bookingId(bookingAcceptanceDto.getBookingId())
                    .driverId(bookingAcceptanceDto.getUserId())
                    .status(RideStatus.STARTED)
                    .startTime(new Date())
                    .build();
            final RideRecord savedRideRecord = rideService.save(rideRecord);
            rideRecordMap.put(savedRideRecord.getDriverId(), Pair.of(savedRideRecord, bookingRecord));


            // Send booking successfully accepted notification to the driver using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", bookingAcceptanceDto.getUserId());
            jsonObject.put("title", "Booking successfully accepted");
            jsonObject.put("body", "You have accepted the booking");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingRecord.getId().toString());
                put("pickupLatitude", bookingRecord.getPickupCoordinate().getLatitude().toString());
                put("pickupLongitude", bookingRecord.getPickupCoordinate().getLongitude().toString());
                put("dropoffLatitude", bookingRecord.getDropoffCoordinate().getLatitude().toString());
                put("dropoffLongitude", bookingRecord.getDropoffCoordinate().getLongitude().toString());
                put("typeCar", bookingRecord.getTypeCar().toString());
                put("price", bookingRecord.getPrice().toString());
                put("paymentMethod", bookingRecord.getPaymentMethod().toString());
                put("createdAt", bookingRecord.getCreatedAt().toString());
                put("rideId", savedRideRecord.toString());
                put("startTime", savedRideRecord.getStartTime().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);


            // Send booking successfully accepted notification to the passenger using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", bookingRecord.getPassengerId());
            jsonObject.put("title", "Booking successfully accepted");
            jsonObject.put("body", "A driver has accepted your booking");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingRecord.getId().toString());
                put("pickupLatitude", bookingRecord.getPickupCoordinate().getLatitude().toString());
                put("pickupLongitude", bookingRecord.getPickupCoordinate().getLongitude().toString());
                put("dropoffLatitude", bookingRecord.getDropoffCoordinate().getLatitude().toString());
                put("dropoffLongitude", bookingRecord.getDropoffCoordinate().getLongitude().toString());
                put("typeCar", bookingRecord.getTypeCar().toString());
                put("price", bookingRecord.getPrice().toString());
                put("paymentMethod", bookingRecord.getPaymentMethod().toString());
                put("createdAt", bookingRecord.getCreatedAt().toString());
                put("rideId", savedRideRecord.toString());
                put("startTime", savedRideRecord.getStartTime().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);
            requestEntity = new HttpEntity<String>(null, requestHeader);


            // Return accepted response
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // @PostMapping("/update_driver_location")
    // public ResponseEntity<BookingAcceptanceDto> updateDriverLocation(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

    // @PostMapping("/finish_ride")
    // public ResponseEntity<BookingAcceptanceDto> finishRide(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

    // @PostMapping("/cancel_booking")
    // public ResponseEntity<BookingAcceptanceDto> cancelBooking(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

    // @PostMapping("/cancel_ride")
    // public ResponseEntity<BookingAcceptanceDto> cancelRide(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
    //     return ResponseEntity.badRequest().build();
    // }

}
