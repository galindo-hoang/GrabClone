package com.example.booking.controller;

import com.example.booking.model.domain.BookingState;
import com.example.booking.model.domain.PaymentMethod;
import com.example.booking.model.domain.RideState;
import com.example.booking.model.domain.TypeCar;
import com.example.booking.model.dto.BookingAcceptanceDto;
import com.example.booking.model.dto.BookingRequestDto;
import com.example.booking.model.dto.BookingRideUpdateDto;
import com.example.booking.model.dto.DriverLocationDto;
import com.example.booking.model.entity.BookingRecord;
import com.example.booking.model.entity.RideRecord;
import com.example.booking.service.BookingStoreService;
import com.example.booking.service.RideStoreService;
import com.example.clients.feign.NotificationRequest.NotificationRequestClient;
import com.example.clients.feign.NotificationRequest.NotificationRequestDto;
import com.example.clients.feign.NotificationRequest.SubscriptionRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@CrossOrigin("localhost:3000")
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    private BookingStoreService bookingService;
    @Autowired
    private RideStoreService rideService;
    @Autowired
    private NotificationRequestClient notificationRequestClient;
    private ObjectMapper Obj = new ObjectMapper();

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


            // Create notification request
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .target("booking")
                    .title("New booking")
                    .body("A new booking is available")
                    .data(new HashMap<>() {
                        {
                            put("bookingId", bookingRecordSaving.toString());
                            put("pickupLocation", Obj.writeValueAsString(bookingDto.getPickupLocation()));
                            put("dropoffLocation", Obj.writeValueAsString(bookingDto.getDropoffLocation()));
                            put("typeCar", bookingDto.getTypeCar());
                            put("price", bookingDto.getPrice().toString());
                            put("paymentMethod", bookingDto.getPaymentMethod());
                        }
                    }).build();

            // Send notification request to FCM service
            notificationRequestClient.sendPnsToTopic(notificationRequestDto);
            // Return success response
            return ResponseEntity.ok(bookingRecordSaving);
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
                    .topicName("booking")
                    .build();
            notificationRequestClient.unsubscribeFromTopic(subscriptionRequestDtoForUnSubscribeDriver);


            // Send booking already accepted notification to all drivers using FCM service
            NotificationRequestDto notificationRejectTopic = NotificationRequestDto.builder()
                    .target("booking")
                    .title("Booking claimed")
                    .body("The booking has been claimed by another driver")
                    .data(new HashMap<>() {
                        {
                            put("bookingId", bookingAcceptanceDto.getBookingId().toString());
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


            // Send booking successfully accepted notification to the driver using FCM service
            Integer rideId = rideRecordSaving.getId();
            Date startTime = rideRecordSaving.getStartTime();
            NotificationRequestDto notificationForDriverAcceptBooking =
                    NotificationRequestDto.builder()
                            .target(bookingAcceptanceDto.getUsername())
                            .title("Booking successfully accepted")
                            .body("You have accepted the booking")
                            .data(new HashMap<>() {
                                {
                                    put("bookingId", bookingRecordSaving.getId().toString());
                                    put("pickupLocation", Obj.writeValueAsString(bookingRecordSaving.getPickupCoordinate()));
                                    put("dropoffLocation", Obj.writeValueAsString(bookingRecordSaving.getDropoffCoordinate()));
                                    put("typeCar", bookingRecordSaving.getTypeCar().toString());
                                    put("price", bookingRecordSaving.getPrice().toString());
                                    put("paymentMethod", bookingRecordSaving.getPaymentMethod().toString());
                                    put("createdAt", bookingRecordSaving.getCreatedAt().toString());
                                    put("rideId", rideId.toString());
                                    put("startTime", startTime.toString());
                                }
                            }).build();
            notificationRequestClient.sendPnsToUser(notificationForDriverAcceptBooking);


            // Send booking successfully accepted notification to the passenger using FCM service
            NotificationRequestDto notificationForUserBooking =
                    NotificationRequestDto.builder()
                            .target(bookingRecordSaving.getPassengerUsername().toString())
                            .title("Booking successfully accepted")
                            .body("A driver has accepted your booking")
                            .data(new HashMap<>() {{
                                put("bookingId", bookingRecordSaving.getId().toString());
                                put("pickupLocation", Obj.writeValueAsString(bookingRecordSaving.getPickupCoordinate()));
                                put("dropoffLocation", Obj.writeValueAsString(bookingRecordSaving.getDropoffCoordinate()));
                                put("typeCar", bookingRecordSaving.getTypeCar().toString());
                                put("price", bookingRecordSaving.getPrice().toString());
                                put("paymentMethod", bookingRecordSaving.getPaymentMethod().toString());
                                put("createdAt", bookingRecordSaving.getCreatedAt().toString());
                                put("rideId", rideId.toString());
                                put("startTime", startTime.toString());
                            }}).build();


            // Return accepted response
            return notificationRequestClient.sendPnsToUser(notificationForUserBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/update_driver_location")
    public ResponseEntity<String> updateDriverLocation(@RequestBody DriverLocationDto driverLocationDto) {
        try {
            // Return not found if ride record not found
            if (!rideRecordMap.containsKey(driverLocationDto.getUsername())) {
                return ResponseEntity.notFound().build();
            }


            // Push the driver's location to the passenger
            NotificationRequestDto notificationForDriverLocation = NotificationRequestDto.builder()
                    .target(rideRecordMap.get(driverLocationDto.getUsername()).getSecond().getPassengerUsername().toString())
                    .title("Update driver location")
                    .body("The driver's location has been updated")
                    .data(new HashMap<>() {
                        {
                            put("rideId", rideRecordMap.get(driverLocationDto.getUsername()).getFirst().toString());
                            put("driverLocation", Obj.writeValueAsString(driverLocationDto.getLocation()));

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
            NotificationRequestDto notificationForDriver = NotificationRequestDto.builder()
                    .target(finishRideDto.getUsername().toString())
                    .title("Ride finished")
                    .body("The ride has been finished")
                    .data(new HashMap<>() {
                        {
                            put("rideId", rideRecordSaving.toString());
                            put("startTime", rideRecordSaving.getStartTime().toString());
                            put("endTime", rideRecordSaving.getEndTime().toString());
                        }
                    }).build();

            notificationRequestClient.sendPnsToUser(notificationForDriver);


            // Send ride finished notification to the passenger using FCM service
            NotificationRequestDto notificationForPassenger = NotificationRequestDto.builder()
                    .target(bookingRecord.getPassengerUsername().toString())
                    .title("Ride finished")
                    .body("The ride has been finished")
                    .data(new HashMap<>() {
                        {
                            put("rideId", rideRecordSaving.toString());
                            put("startTime", rideRecordSaving.getStartTime().toString());
                            put("endTime", rideRecordSaving.getEndTime().toString());
                        }
                    }).build();

            notificationRequestClient.sendPnsToUser(notificationForPassenger);


            // Re-subscribe the driver to the booking topic
            SubscriptionRequestDto subscriptionToDriverForNextRide = SubscriptionRequestDto.builder()
                    .username(finishRideDto.getUsername())
                    .topicName("booking")
                    .build();

            notificationRequestClient.subscribeToTopic(subscriptionToDriverForNextRide);

            // Return success response
            return notificationRequestClient.sendPnsToUser(notificationForPassenger);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
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
            NotificationRequestDto notificationForDriverCanceled = NotificationRequestDto.builder()
                    .target("booking")
                    .title("Booking cancelled")
                    .body("The booking has been cancelled")
                    .data(new HashMap<>() {
                        {
                            put("bookingId", bookingId.toString());
                            put("createdAt", createdAt.toString());
                            put("endedAt", endedAt.toString());
                        }
                    }).build();
            notificationRequestClient.sendPnsToTopic(notificationForDriverCanceled);


            // Update booking record and save it to database
            bookingRecord.setState(BookingState.CANCELLED);
            bookingRecord.setUpdatedAt(endedAt);
            BookingRecord bookingRecordSaving = bookingService.save(bookingRecord);


            // Send booking cancelled notification to the passenger using FCM service
            NotificationRequestDto notificationForPassenger = NotificationRequestDto.builder()
                    .target(bookingRecordSaving.getPassengerUsername().toString())
                    .title("Booking cancelled")
                    .body("The booking has been cancelled")
                    .data(new HashMap<>() {
                        {
                            put("bookingId", bookingId.toString());
                            put("createdAt", createdAt.toString());
                            put("endedAt", endedAt.toString());
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
            NotificationRequestDto notificationCancelRideForPassenger = NotificationRequestDto.builder()
                    .target(bookingRecord.getPassengerUsername().toString())
                    .title("Ride cancelled")
                    .body("The ride has been cancelled. Please wait for another driver")
                    .data(new HashMap<>() {
                        {
                            put("rideId", rideRecord.getId().toString());
                            put("startTime", rideRecord.getStartTime().toString());
                            put("endTime", rideRecord.getEndTime().toString());
                        }
                    }).build();
            notificationRequestClient.sendPnsToUser(notificationCancelRideForPassenger);


            // Update booking record and save it to database
            bookingRecord.setState(BookingState.REJECTED);
            bookingRecord.setUpdatedAt(new Date());
            BookingRecord bookingRecordSaving = bookingService.save(bookingRecord);
            bookingRecordMap.put(bookingRecord.getId(), bookingRecord);


            // Push notification to other drivers using FCM service
            NotificationRequestDto notificationForOtherDrivers = NotificationRequestDto.builder()
                    .target("booking")
                    .title("New booking")
                    .body("A new booking is available")
                    .data(new HashMap<>() {
                        {
                            put("bookingId", bookingRecordSaving.toString());
                            put("pickupLocation", Obj.writeValueAsString(bookingRecordSaving.getPickupCoordinate()));
                            put("dropoffLocation", Obj.writeValueAsString(bookingRecordSaving.getDropoffCoordinate()));
                            put("typeCar", bookingRecordSaving.getTypeCar().toString());
                            put("paymentMethod", bookingRecordSaving.getPaymentMethod().toString());
                            put("price", bookingRecordSaving.getPrice().toString());
                        }
                    }).build();

            notificationRequestClient.sendPnsToTopic(notificationForOtherDrivers);


            // Update ride record and save it to database
            rideRecord.setRideState(RideState.CANCELLED);
            RideRecord rideRecordSaving = rideService.save(rideRecord);


            // Send ride cancelled notification to driver using FCM service
            NotificationRequestDto notificationDriverCanceled = NotificationRequestDto.builder()
                    .target(rideRecordSaving.getDriverUsername())
                    .title("Ride cancelled")
                    .body("The ride has been cancelled")
                    .data(new HashMap<>() {
                        {
                            put("rideId", rideRecordSaving.getId().toString());
                            put("startTime", rideRecordSaving.getStartTime().toString());
                            put("endTime", rideRecordSaving.getEndTime().toString());
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
}