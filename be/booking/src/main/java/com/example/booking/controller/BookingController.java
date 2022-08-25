package com.example.booking.controller;

import com.example.booking.model.domain.*;
import com.example.booking.model.dto.*;
import com.example.booking.model.entity.BookingRecord;
import com.example.booking.model.entity.DriverRecord;
import com.example.booking.model.entity.RideRecord;
import com.example.booking.service.BookingStoreService;
import com.example.booking.service.DistanceApiService;
import com.example.booking.service.DriverStoreService;
import com.example.booking.service.RideStoreService;
import com.example.clients.feign.DriverLocation.DriverLocationDto;
import com.example.clients.feign.NotificationRequest.NotificationRequestClient;
import com.example.clients.feign.NotificationRequest.NotificationRequestDto;
import com.example.clients.feign.NotificationRequest.SubscriptionRequestDto;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    private BookingStoreService bookingService;
    @Autowired
    private RideStoreService rideService;
    @Autowired
    private NotificationRequestClient notificationRequestClient;
    @Autowired
    private DriverStoreService driverStoreService;
    private DistanceApiService distanceApiService = new DistanceApiService();
    // The key is the booking id
    private HashMap<Integer, BookingRecord> bookingRecordMap;
    // The key is the driver username
    private HashMap<String, Pair<RideRecord, BookingRecord>> rideRecordMap;

    public BookingController() {
        bookingRecordMap = new HashMap<>();
        rideRecordMap = new HashMap<>();
    }

    // Create a booking for a client vs call center
    @PostMapping("/create_booking")
    public ResponseEntity<BookingRecord> createBooking(@RequestBody BookingRequestDto bookingDto) {
        try {
            // Reject creating a booking if the passenger has created a booking
            for (HashMap.Entry<Integer, BookingRecord> entry : bookingRecordMap.entrySet()) {
                if (entry.getValue().getPhonenumber().equals(bookingDto.getPhonenumber())) {
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
                }
            }

            for (HashMap.Entry<String, Pair<RideRecord, BookingRecord>> entry : rideRecordMap.entrySet()) {
                if (entry.getValue().getSecond().getPhonenumber().equals(bookingDto.getPhonenumber())) {
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
                }
            }


            // Create booking record and save it to database
            BookingRecord bookingRecord = BookingRecord.builder()
                    .passengerUsername(bookingDto.getUsername())
                    .phonenumber(bookingDto.getPhonenumber())
                    .pickupCoordinate(bookingDto.getPickupLocation())
                    .dropoffCoordinate(bookingDto.getDropoffLocation())
                    .typeCar(TypeCar.valueOf(bookingDto.getTypeCar()))
                    .state(BookingState.CREATED)
                    .paymentMethod(PaymentMethod.valueOf(bookingDto.getPaymentMethod()))
                    .price(bookingDto.getPrice())
                    .createdAt(new Date())
                    .build();
            BookingRecord bookingRecordSaving = bookingService.save(bookingRecord);
            bookingRecordMap.put(bookingRecordSaving.getId(), bookingRecordSaving);

            //Get all driver is subscribed
            List<DriverRecord> driverRecords = driverStoreService.driverRecordList();
            List<DriverRecord> driverRecordsInArea = driverRecords.stream().filter(driverRecord ->
                    driverRecord.getLocation() != null &&
                            distanceApiService.getDistance(bookingDto.getPickupLocation()
                                    , driverRecord.getLocation()) < 4000).collect(Collectors.toList());
            if (driverRecordsInArea.size() != 0) {
                //Subscribe drivers
                for (DriverRecord driverRecord : driverRecordsInArea) {
                    SubscriptionRequestDto subscriptionRequestDto = SubscriptionRequestDto.builder()
                            .username(driverRecord.getDriverUsername())
                            .topicName("booking-" + bookingRecordSaving.getId())
                            .build();
                    notificationRequestClient.subscribeToTopic(subscriptionRequestDto);
                }

                //Create booking creation
                BookingRecordDto bookingCreation = BookingRecordDto.builder()
                        .bookingId(bookingRecordSaving.getId())
                        .pickupLocation(bookingRecordSaving.getPickupCoordinate())
                        .dropoffLocation(bookingRecordSaving.getDropoffCoordinate())
                        .typeCar(bookingRecordSaving.getTypeCar())
                        .price(bookingRecordSaving.getPrice())
                        .paymentMethod(bookingRecordSaving.getPaymentMethod())
                        .createdAt(bookingRecordSaving.getCreatedAt())
                        .build();
                String jsonBookingCreation = new Gson().toJson(bookingCreation);

                // Create notification request
                NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                        .target("booking-" + bookingRecordSaving.getId())
                        .title("New booking")
                        .body("A new booking is available")
                        .data(new HashMap<>() {
                            {
                                put("booking-" + bookingRecordSaving.getId(), jsonBookingCreation);
                            }
                        }).build();

                // Send notification request to FCM service
                notificationRequestClient.sendPnsToTopic(notificationRequestDto);

                // Return success response
                return ResponseEntity.ok(bookingRecordSaving);
            } else {
                bookingRecordMap.remove(bookingRecordSaving.getId());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accept_booking")
    public ResponseEntity<String> acceptBooking(@RequestBody BookingAcceptanceDto bookingAcceptanceDto) {
        try {
            // Return not found if booking record not found
            if (!bookingRecordMap.containsKey(bookingAcceptanceDto.getBookingId())) {
                return ResponseEntity.notFound().build();
            }


            // Reject if the driver is already assigned to another booking
            if (rideRecordMap.containsKey(bookingAcceptanceDto.getUsername())) {
                return ResponseEntity.badRequest().build();
            }


            // Unsubscribe the accepted driver from the booking topic
            SubscriptionRequestDto subscriptionRequestDtoForUnSubscribeDriver = SubscriptionRequestDto.builder()
                    .username(bookingAcceptanceDto.getUsername())
                    .topicName("booking-" + bookingAcceptanceDto.getBookingId())
                    .build();
            notificationRequestClient.unsubscribeFromTopic(subscriptionRequestDtoForUnSubscribeDriver);


            //update start time for driver when accept
            bookingAcceptanceDto.setAcceptanceDriverDateTime(new Date());
            // Send booking already accepted notification to all drivers using FCM service
            NotificationRequestDto notificationRejectTopic = NotificationRequestDto.builder()
                    .target("booking-" + bookingAcceptanceDto.getBookingId())
                    .title("Booking claimed")
                    .body("The booking has been claimed by another driver")
                    .data(new HashMap<>() {
                        {
                            put("booking", new Gson().toJson(bookingAcceptanceDto));
                        }
                    }).build();
            notificationRequestClient.sendPnsToTopic(notificationRejectTopic);


            // Update booking state to FINISHED and save it to database
            BookingRecord bookingRecord = bookingRecordMap.remove(bookingAcceptanceDto.getBookingId());
            bookingRecord.setState(BookingState.ACCEPTED);
            BookingRecord bookingRecordSaving = bookingService.save(bookingRecord);


            // Create ride record and save it to database
            RideRecord rideRecord = RideRecord.builder()
                    .bookingId(bookingAcceptanceDto.getBookingId())
                    .driverUsername(bookingAcceptanceDto.getUsername())
                    .rideState(RideState.STARTED)
                    .startTime(new Date())
                    .build();
            RideRecord rideRecordSaving = rideService.save(rideRecord);
            rideRecordMap.put(rideRecordSaving.getDriverUsername(), Pair.of(rideRecordSaving, bookingRecordSaving));


            // Create booking acceptance
            Integer rideId = rideRecordSaving.getId();
            Date startTime = rideRecordSaving.getStartTime();
            BookingRecordDto    bookingCreationAcceptance = BookingRecordDto.builder()
                    .bookingId(bookingRecordSaving.getId())
                    .pickupLocation(bookingRecordSaving.getPickupCoordinate())
                    .dropoffLocation(bookingRecordSaving.getDropoffCoordinate())
                    .typeCar(bookingRecordSaving.getTypeCar())
                    .price(bookingRecordSaving.getPrice())
                    .paymentMethod(bookingRecordSaving.getPaymentMethod())
                    .createdAt(bookingRecordSaving.getCreatedAt())
                    .rideId(rideId)
                    .startTime(startTime)
                    .build();
            String jsonBookingCreationAcceptance = new Gson().toJson(bookingCreationAcceptance);
            // Send booking successfully accepted notification to the driver using FCM service
            NotificationRequestDto notificationForDriverAcceptBooking =
                    NotificationRequestDto.builder()
                            .target(bookingAcceptanceDto.getUsername())
                            .title("Booking successfully accepted")
                            .body("You have accepted the booking")
                            .data(new HashMap<>() {
                                {
                                    put("booking", jsonBookingCreationAcceptance);
                                }
                            }).build();
            notificationRequestClient.sendPnsToUser(notificationForDriverAcceptBooking);

            BookingRecordDto bookingRecordDto = BookingRecordDto.builder()
                    .bookingId(bookingRecordSaving.getId())
                    .pickupLocation(bookingRecordSaving.getPickupCoordinate())
                    .dropoffLocation(bookingRecordSaving.getDropoffCoordinate())
                    .typeCar(bookingRecordSaving.getTypeCar())
                    .price(bookingRecordSaving.getPrice())
                    .paymentMethod(bookingRecordSaving.getPaymentMethod())
                    .createdAt(bookingRecordSaving.getCreatedAt())
                    .rideId(rideId)
                    .startTime(startTime)
                    .build();
            String jsonBookingRecordDto = new Gson().toJson(bookingRecordDto);

            // Send booking successfully accepted notification to the passenger using FCM service
            NotificationRequestDto notificationForUserBooking =
                    NotificationRequestDto.builder()
                            .target(bookingRecordSaving.getPassengerUsername())
                            .title("Booking successfully accepted")
                            .body("A driver has accepted your booking")
                            .data(new HashMap<>() {{
                                put("booking", jsonBookingRecordDto);
                            }}).build();

            notificationRequestClient.sendPnsToUser(notificationForUserBooking);
            // Return accepted response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update_location")
    public ResponseEntity<DriverRecord> updateLocation(@RequestBody DriverLocationDto driverLocationDto) {
        return ResponseEntity.ok(driverStoreService.updateLocationService(driverLocationDto));
    }

    @PostMapping("/update_driver_location")
    public ResponseEntity<String> updateDriverLocation(@RequestBody DriverLocationDto driverLocationDto) {
        try {

            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(driverLocationDto.getUsername())) {
                return ResponseEntity.notFound().build();
            }


            // Push the driver's location to the passenger
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rideId", rideRecordMap.get(driverLocationDto.getUsername()).getFirst());
            jsonObject.put("driverLocation", new Gson().toJson(driverLocationDto.getLocation()));
            NotificationRequestDto notificationForDriverLocation = NotificationRequestDto.builder()
                    .target(rideRecordMap.get(driverLocationDto.getUsername()).getSecond().getPassengerUsername())
                    .title("Update driver location")
                    .body("The driver's location has been updated")
                    .data(new HashMap<>() {
                        {
                            put("ride", jsonObject.toString());
                        }
                    }).build();


            // Return success response
            return notificationRequestClient.sendPnsToUser(notificationForDriverLocation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/finish_ride")
    public ResponseEntity<String> finishRide(@RequestBody BookingRideUpdateDto finishRideDto) {
        try {
            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(finishRideDto.getUsername())) {
                return ResponseEntity.notFound().build();
            }


            // Update ride record and save it to database
            RideRecord rideRecord = rideRecordMap.get(finishRideDto.getUsername()).getFirst();
            BookingRecord bookingRecord = rideRecordMap.remove(finishRideDto.getUsername()).getSecond();
            rideRecord.setRideState(RideState.FINISHED);
            rideRecord.setEndTime(new Date());
            RideRecord rideRecordSaving = rideService.save(rideRecord);


            // Send ride finished notification to the driver using FCM service
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rideId", rideRecordSaving.getId());
            jsonObject.put("startTime", rideRecordSaving.getStartTime());
            jsonObject.put("endTime", rideRecordSaving.getEndTime());
            NotificationRequestDto notificationForDriver = NotificationRequestDto.builder()
                    .target(finishRideDto.getUsername())
                    .title("Ride finished")
                    .body("The ride has been finished")
                    .data(new HashMap<>() {
                        {
                            put("finish_ride", jsonObject.toString());
                        }
                    }).build();

            notificationRequestClient.sendPnsToUser(notificationForDriver);


            // Send ride finished notification to the passenger using FCM service
            JSONObject jsonObjectFinished = new JSONObject();
            jsonObjectFinished.put("rideId", rideRecordSaving.getId());
            jsonObjectFinished.put("startTime", rideRecordSaving.getStartTime());
            jsonObjectFinished.put("endTime", rideRecordSaving.getEndTime());
            NotificationRequestDto notificationForPassenger = NotificationRequestDto.builder()
                    .target(bookingRecord.getPassengerUsername())
                    .title("Ride finished")
                    .body("The ride has been finished")
                    .data(new HashMap<>() {
                        {
                            put("finish_ride", jsonObjectFinished.toString());
                        }
                    }).build();

            notificationRequestClient.sendPnsToUser(notificationForPassenger);

            // Return success response
            notificationRequestClient.sendPnsToUser(notificationForPassenger);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cancel_booking")
    public ResponseEntity<String> cancelBooking(@RequestBody BookingRideUpdateDto cancelBookingDto) {
        try {
            // Get booking record of the user
            BookingRecord bookingRecord = null;
            for (HashMap.Entry<Integer, BookingRecord> entry : bookingRecordMap.entrySet()) {
                if (entry.getValue().getPassengerUsername().equals(cancelBookingDto.getUsername())) {
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
            jsonObject.put("bookingId", bookingId);
            jsonObject.put("createdAt", createdAt);
            jsonObject.put("endedAt", endedAt);

            NotificationRequestDto notificationForDriverCanceled = NotificationRequestDto.builder()
                    .target("booking-" + bookingId)
                    .title("Booking cancelled")
                    .body("The booking has been cancelled")
                    .data(new HashMap<>() {
                        {
                            put("booking", jsonObject.toString());
                        }
                    }).build();
            notificationRequestClient.sendPnsToTopic(notificationForDriverCanceled);


            // Update booking record and save it to database
            bookingRecord.setState(BookingState.CANCELLED);
            bookingRecord.setUpdatedAt(endedAt);
            BookingRecord bookingRecordSaving = bookingService.save(bookingRecord);


            // Send booking cancelled notification to the passenger using FCM service
            JSONObject jsonObjectCancel = new JSONObject();
            jsonObjectCancel.put("bookingId", bookingId);
            jsonObjectCancel.put("createdAt", createdAt);
            jsonObjectCancel.put("endedAt", endedAt);
            NotificationRequestDto notificationForPassenger = NotificationRequestDto.builder()
                    .target(bookingRecordSaving.getPassengerUsername())
                    .title("Booking cancelled")
                    .body("The booking has been cancelled")
                    .data(new HashMap<>() {
                        {
                            put("booking", jsonObjectCancel.toString());
                        }
                    }).build();


            // Return success response
            return notificationRequestClient.sendPnsToUser(notificationForPassenger);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cancel_ride")
    public ResponseEntity<String> cancelRide(@RequestBody BookingRideUpdateDto cancelRideDto) {
        try {
            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(cancelRideDto.getUsername())) {
                return ResponseEntity.notFound().build();
            }


            RideRecord rideRecord = rideRecordMap.get(cancelRideDto.getUsername()).getFirst();
            BookingRecord bookingRecord = rideRecordMap.remove(cancelRideDto.getUsername()).getSecond();
            rideRecord.setEndTime(new Date());


            // Send ride cancelled notification to the passenger using FCM service
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rideId", rideRecord.getId());
            jsonObject.put("startTime", rideRecord.getStartTime());
            jsonObject.put("endTime", rideRecord.getEndTime());
            NotificationRequestDto notificationCancelRideForPassenger = NotificationRequestDto.builder()
                    .target(bookingRecord.getPassengerUsername())
                    .title("Ride cancelled")
                    .body("The ride has been cancelled. Please wait for another driver")
                    .data(new HashMap<>() {
                        {
                            put("ride", jsonObject.toString());
                        }
                    }).build();
            notificationRequestClient.sendPnsToUser(notificationCancelRideForPassenger);


            // Update booking record and save it to database
            bookingRecord.setState(BookingState.REJECTED);
            bookingRecord.setUpdatedAt(new Date());
            BookingRecord bookingRecordSaving = bookingService.save(bookingRecord);
            bookingRecordMap.put(bookingRecord.getId(), bookingRecord);


            // Push notification to other drivers using FCM service
            BookingRecordDto bookingRecordDto = BookingRecordDto.builder()
                    .bookingId(bookingRecordSaving.getId())
                    .pickupLocation(bookingRecordSaving.getPickupCoordinate())
                    .dropoffLocation(bookingRecordSaving.getDropoffCoordinate())
                    .typeCar(bookingRecordSaving.getTypeCar())
                    .paymentMethod(bookingRecordSaving.getPaymentMethod())
                    .price(bookingRecordSaving.getPrice())
                    .build();
            NotificationRequestDto notificationForOtherDrivers = NotificationRequestDto.builder()
                    .target("booking")
                    .title("New booking")
                    .body("A new booking is available")
                    .data(new HashMap<>() {
                        {
                            put("booking", new Gson().toJson(bookingRecordDto));
                        }
                    }).build();

            notificationRequestClient.sendPnsToTopic(notificationForOtherDrivers);


            // Update ride record and save it to database
            rideRecord.setRideState(RideState.CANCELLED);
            RideRecord rideRecordSaving = rideService.save(rideRecord);


            // Send ride cancelled notification to driver using FCM service
            JSONObject jsonObjectCancel = new JSONObject();
            jsonObjectCancel.put("rideId", rideRecordSaving.getId());
            jsonObjectCancel.put("startTime", rideRecordSaving.getStartTime());
            jsonObjectCancel.put("endTime", rideRecordSaving.getEndTime());
            NotificationRequestDto notificationDriverCanceled = NotificationRequestDto.builder()
                    .target(rideRecordSaving.getDriverUsername())
                    .title("Ride cancelled")
                    .body("The ride has been cancelled")
                    .data(new HashMap<>() {
                        {
                            put("ride", jsonObjectCancel.toString());
                        }
                    }).build();

            notificationRequestClient.sendPnsToUser(notificationDriverCanceled);


            // Re-subscribe the driver to the booking topic
            SubscriptionRequestDto subscriptionToReSubscribeForDriverBooking = SubscriptionRequestDto.builder()
                    .username(rideRecord.getDriverUsername())
                    .topicName("booking")
                    .build();
            notificationRequestClient.subscribeToTopic(subscriptionToReSubscribeForDriverBooking);

            // Return success response
            return notificationRequestClient.sendPnsToUser(notificationDriverCanceled);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/topDepartures")
    public ResponseEntity<List<BookingResponseDto>> topDepartures(@RequestBody TopLocationRequestDto request) {
        try {
            // Get Booking Records according phonenumber
            return new ResponseEntity<>(bookingService.findTopDeparture(request.getPhoneNumber(), request.getLimit()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/topDestination")
    public ResponseEntity<List<BookingResponseDto>> topDestination(@RequestBody TopLocationRequestDto request) {
        try {
            // Get Booking Records according phonenumber
            return new ResponseEntity<>(bookingService.findTopDestination(request.getPhoneNumber(), request.getLimit()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingRecordDto> getBooking(@PathVariable Integer bookingId) {
        try {
            // Get Booking Record according bookingId
            BookingRecord bookingRecord = bookingService.findById(bookingId);
            System.out.println(bookingRecord);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}