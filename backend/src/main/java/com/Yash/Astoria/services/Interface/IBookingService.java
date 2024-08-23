package com.Yash.Astoria.services.Interface;

import com.Yash.Astoria.dto.Response;
import com.Yash.Astoria.entities.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
