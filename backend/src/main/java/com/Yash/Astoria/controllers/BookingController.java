package com.Yash.Astoria.controllers;

import com.Yash.Astoria.dto.Response;
import com.Yash.Astoria.entities.Booking;
import com.Yash.Astoria.services.Interface.IBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable("roomId") Long roomId,
                                                 @PathVariable("userId") Long userId,
                                                 @Valid
                                                 @RequestBody Booking bookingRequest) {

        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);

        System.out.println("Check-out Date: " + bookingRequest.getCheckOutDate());

        System.out.println("Check-out Date: " + bookingRequest.getCheckInDate());

        System.out.println("Check-out Date: " + bookingRequest.getNumOfAdults());

        System.out.println("Check-out Date: " + bookingRequest.getNumOfAdults());

        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {
        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
