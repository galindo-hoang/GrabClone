package com.example.booking.controller;

import com.example.booking.model.domain.*;
import com.example.booking.model.dto.*;
import com.example.booking.model.entity.*;
import com.example.booking.service.*;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping("/top_arrival")
    public ResponseEntity<TopCoorRespDto> getTopArrivals(@RequestBody TopCoorReqDto topArrivalDto) {
        try {
            // Reject if number of arrivals is too large or < 1
            if (topArrivalDto.getCount() > 10 || topArrivalDto.getCount() < 1) {
                return ResponseEntity.badRequest().build();
            }


            // Get the top arrivals
            List<BookingRecord> bookingRecords = bookingService.findByUserIdAndPhoneNumber(topArrivalDto.getUserId(), topArrivalDto.getPhoneNumber());
            HashMap<MapCoordinate, Integer> mapCoordinateCount = new HashMap<>();

            for (BookingRecord bookingRecord : bookingRecords) {
                MapCoordinate dropoffCoordinate = bookingRecord.getDropoffCoordinate();
                if (mapCoordinateCount.containsKey(dropoffCoordinate)) {
                    mapCoordinateCount.put(dropoffCoordinate, mapCoordinateCount.get(dropoffCoordinate) + 1);
                } else {
                    mapCoordinateCount.put(dropoffCoordinate, 1);
                }
            }

            // Sort the map by value
            HashMap<MapCoordinate, Integer> sortedMap = mapCoordinateCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            // Get the top topArrivalDto
            TopCoorRespDto topCoorRespDto = new TopCoorRespDto();
            int count = 0;
            for (Map.Entry<MapCoordinate, Integer> entry : sortedMap.entrySet()) {
                if (count >= topArrivalDto.getCount()) {
                    break;
                }
                topCoorRespDto.getTopCoordinates().add(entry.getKey());
                count++;
            }


            // Return the top topArrivalDto
            return ResponseEntity.ok(topCoorRespDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/top_destinations")
    public ResponseEntity<TopCoorRespDto> getTopDestinations(@RequestBody TopCoorReqDto topDestinationDto) {
        try {
            // Reject if number of destinations is too large or < 1
            if (topDestinationDto.getCount() > 10 || topDestinationDto.getCount() < 1) {
                return ResponseEntity.badRequest().build();
            }


            // Get the top destinations
            List<BookingRecord> bookingRecords = bookingService.findByUserIdAndPhoneNumber(topDestinationDto.getUserId(), topDestinationDto.getPhoneNumber());
            HashMap<MapCoordinate, Integer> mapCoordinateCount = new HashMap<>();

            for (BookingRecord bookingRecord : bookingRecords) {
                MapCoordinate dropoffCoordinate = bookingRecord.getDropoffCoordinate();
                if (mapCoordinateCount.containsKey(dropoffCoordinate)) {
                    mapCoordinateCount.put(dropoffCoordinate, mapCoordinateCount.get(dropoffCoordinate) + 1);
                } else {
                    mapCoordinateCount.put(dropoffCoordinate, 1);
                }
            }

            // Sort the map by value
            HashMap<MapCoordinate, Integer> sortedMap = mapCoordinateCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            // Get the top destinations
            TopCoorRespDto topCoorRespDto = new TopCoorRespDto();
            int count = 0;
            for (Map.Entry<MapCoordinate, Integer> entry : sortedMap.entrySet()) {
                if (count >= topDestinationDto.getCount()) {
                    break;
                }
                topCoorRespDto.getTopCoordinates().add(entry.getKey());
                count++;
            }


            // Return the top destinations
            return ResponseEntity.ok(topCoorRespDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
                    .state(BookingState.CREATED)
                    .paymentMethod(PaymentMethod.valueOf(bookingDto.getPaymentMethod()))
                    .price(bookingDto.getPrice())
                    .createdAt(new Date())
                    .build();
            bookingRecord = bookingService.save(bookingRecord);
            bookingRecordMap.put(bookingRecord.getId(), bookingRecord);


            // Send booking notification to all drivers using FCM service
            Integer bookingId = bookingRecord.getId();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", "booking");
            jsonObject.put("title", "New booking");
            jsonObject.put("body", "A new booking is available");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingId.toString());
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


            // Reject if the driver is already assigned to another booking
            if (rideRecordMap.containsKey(bookingAcceptanceDto.getUserId())) {
                return ResponseEntity.badRequest().build();
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


            // Update booking state to FINISHED and save it to database
            BookingRecord bookingRecord = bookingRecordMap.remove(bookingAcceptanceDto.getBookingId());
            bookingRecord.setState(BookingState.ACCEPTED);
            bookingService.save(bookingRecord);
            

            // Create ride record and save it to database
            RideRecord rideRecord = RideRecord.builder()
                    .bookingId(bookingAcceptanceDto.getBookingId())
                    .driverId(bookingAcceptanceDto.getUserId())
                    .state(RideState.STARTED)
                    .startTime(new Date())
                    .build();
            rideRecord = rideService.save(rideRecord);
            rideRecordMap.put(rideRecord.getDriverId(), Pair.of(rideRecord, bookingRecord));


            // Send booking successfully accepted notification to the driver using FCM service
            Integer rideId = rideRecord.getId();
            Date startTime = rideRecord.getStartTime();
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
                put("rideId", rideId.toString());
                put("startTime", startTime.toString());
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
                put("rideId", rideId.toString());
                put("startTime", startTime.toString());
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

    @PostMapping("/update_driver_location")
    public ResponseEntity<DriverLocationDto> updateDriverLocation(@RequestBody DriverLocationDto driverLocationDto) {
        try {
            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(driverLocationDto.getUserId())) {
                return ResponseEntity.notFound().build();
            }


            // Push the driver's location to the passenger
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", rideRecordMap.get(driverLocationDto.getUserId()).getSecond().getPassengerId());
            jsonObject.put("title", "Update driver location");
            jsonObject.put("body", "The driver's location has been updated");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("rideId", rideRecordMap.get(driverLocationDto.getUserId()).getFirst().toString());
                put("latitude", driverLocationDto.getLatitude().toString());
                put("longitude", driverLocationDto.getLongitude().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);
            requestEntity = new HttpEntity<String>(null, requestHeader);


            // Return success response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/finish_ride")
    public ResponseEntity<BookingRideUpdateDto> finishRide(@RequestBody BookingRideUpdateDto finishRideDto) {
        try {
            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(finishRideDto.getUserId())) {
                return ResponseEntity.notFound().build();
            }


            // Update ride record and save it to database
            RideRecord rideRecord = rideRecordMap.get(finishRideDto.getUserId()).getFirst();
            BookingRecord bookingRecord = rideRecordMap.remove(finishRideDto.getUserId()).getSecond();
            rideRecord.setState(RideState.FINISHED);
            rideRecord.setEndTime(new Date());
            rideService.save(rideRecord);


            // Send ride finished notification to the driver using FCM service
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", finishRideDto.getUserId());
            jsonObject.put("title", "Ride finished");
            jsonObject.put("body", "The ride has been finished");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("rideId", rideRecord.toString());
                put("startTime", rideRecord.getStartTime().toString());
                put("endTime", rideRecord.getEndTime().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);


            // Send ride finished notification to the passenger using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", bookingRecord.getPassengerId());
            jsonObject.put("title", "Ride finished");
            jsonObject.put("body", "The ride has been finished");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("rideId", rideRecord.toString());
                put("startTime", rideRecord.getStartTime().toString());
                put("endTime", rideRecord.getEndTime().toString());
            }});
            
            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);

            
            // Re-subscribe the driver to the booking topic
            jsonObject = new JSONObject();
            jsonObject.put("userId", finishRideDto.getUserId());
            jsonObject.put("topic", "booking");
            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/subscribe", HttpMethod.POST, requestEntity, Integer.class);
            requestEntity = new HttpEntity<String>(null, requestHeader);


            // Return success response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cancel_booking")
    public ResponseEntity<BookingRideUpdateDto> cancelBooking(@RequestBody BookingRideUpdateDto cancelBookingDto) {
        try {
            // Get booking record of the user
            BookingRecord bookingRecord = null;
            for (HashMap.Entry<Integer, BookingRecord> entry : bookingRecordMap.entrySet()) {
                if (entry.getValue().getPassengerId().equals(cancelBookingDto.getUserId())) {
                    bookingRecord = entry.getValue();
                    bookingRecordMap.remove(entry.getKey());
                    break;
                }
            }


            // Return not found if booking record not found (the user is not a passenger, or the ride has created)
            if (bookingRecord == null) {
                return ResponseEntity.notFound().build();
            }


            // Send booking cancelled notification to drivers using FCM service
            Integer bookingId = bookingRecord.getId();
            Date createdAt = bookingRecord.getCreatedAt();
            Date endedAt = new Date();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", "booking");
            jsonObject.put("title", "Booking cancelled");
            jsonObject.put("body", "The booking has been cancelled");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingId.toString());
                put("createdAt", createdAt.toString());
                put("endedAt", endedAt.toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8082/fcm/topic", HttpMethod.POST, requestEntity, Integer.class);


            // Update booking record and save it to database
            bookingRecord.setState(BookingState.CANCELLED);
            bookingRecord.setUpdatedAt(endedAt);
            bookingService.save(bookingRecord);
            

            // Send booking cancelled notification to the passenger using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", bookingRecord.getPassengerId());
            jsonObject.put("title", "Booking cancelled");
            jsonObject.put("body", "Your booking has been cancelled");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingId.toString());
                put("createdAt", createdAt.toString());
                put("endedAt", endedAt.toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);
            requestEntity = new HttpEntity<String>(null, requestHeader);


            // Return success response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cancel_ride")
    public ResponseEntity<BookingRideUpdateDto> cancelRide(@RequestBody BookingRideUpdateDto cancelRideDto) {
        try {
            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(cancelRideDto.getUserId())) {
                return ResponseEntity.notFound().build();
            }


            RideRecord rideRecord = rideRecordMap.get(cancelRideDto.getUserId()).getFirst();
            BookingRecord bookingRecord = rideRecordMap.remove(cancelRideDto.getUserId()).getSecond();
            rideRecord.setEndTime(new Date());


            // Send ride cancelled notification to the passenger using FCM service
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", bookingRecord.getPassengerId());
            jsonObject.put("title", "Ride cancelled");
            jsonObject.put("body", "The ride has been cancelled. Please wait for another driver");
            jsonObject.put("data", new HashMap<String, String>() {{
                put("rideId", rideRecord.getId().toString());
                put("startTime", rideRecord.getStartTime().toString());
                put("endTime", rideRecord.getEndTime().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);


            // Update booking record and save it to database
            bookingRecord.setState(BookingState.REJECTED);
            bookingRecord.setUpdatedAt(new Date());
            bookingService.save(bookingRecord);
            bookingRecordMap.put(bookingRecord.getId(), bookingRecord);


            // Push notification to other drivers using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", "booking");
            jsonObject.put("title", "New booking");
            jsonObject.put("body", "A new booking is available");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("bookingId", bookingRecord.toString());
                put("pickupLatitude", bookingRecord.getPickupCoordinate().getLatitude().toString());
                put("pickupLongitude", bookingRecord.getPickupCoordinate().getLongitude().toString());
                put("dropoffLatitude", bookingRecord.getDropoffCoordinate().getLatitude().toString());
                put("dropoffLongitude", bookingRecord.getDropoffCoordinate().getLongitude().toString());
                put("typeCar", bookingRecord.getTypeCar().toString());
                put("paymentMethod", bookingRecord.getPaymentMethod().toString());
                put("price", bookingRecord.getPrice().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/topic", HttpMethod.POST, requestEntity, Integer.class);


            // Update ride record and save it to database
            rideRecord.setState(RideState.CANCELLED);
            rideService.save(rideRecord);


            // Send ride cancelled notification to drivers using FCM service
            jsonObject = new JSONObject();
            jsonObject.put("target", rideRecord.getDriverId());
            jsonObject.put("title", "Ride cancelled");
            jsonObject.put("body", "The ride has been cancelled");
            jsonObject.put("data", new HashMap<String, String>() {{ 
                put("rideId", rideRecord.getId().toString());
                put("startTime", rideRecord.getStartTime().toString());
                put("endTime", rideRecord.getEndTime().toString());
            }});

            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);
            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/user", HttpMethod.POST, requestEntity, Integer.class);


            // Re-subscribe the driver to the booking topic
            jsonObject = new JSONObject();
            jsonObject.put("userId", rideRecord.getDriverId());
            jsonObject.put("topic", "booking");
            requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeader);

            response = restTemplate.exchange(
                    "http://localhost:8082/fcm/subscribe", HttpMethod.POST, requestEntity, Integer.class);
            requestEntity = new HttpEntity<String>(null, requestHeader);


            // Return success response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
