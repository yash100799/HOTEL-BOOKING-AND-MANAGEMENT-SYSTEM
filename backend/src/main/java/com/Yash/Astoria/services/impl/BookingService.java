package com.Yash.Astoria.services.impl;

import com.Yash.Astoria.dto.BookingDTO;
import com.Yash.Astoria.dto.Response;
import com.Yash.Astoria.entities.Booking;
import com.Yash.Astoria.entities.Room;
import com.Yash.Astoria.entities.User;
import com.Yash.Astoria.exception.OurException;
import com.Yash.Astoria.repository.BookingRepository;
import com.Yash.Astoria.repository.RoomRepository;
import com.Yash.Astoria.repository.UserRepository;
import com.Yash.Astoria.services.Interface.IBookingService;
import com.Yash.Astoria.services.Interface.IRoomService;
import com.Yash.Astoria.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        Response response = new Response();

        try {

            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check out date must come after Check in date");
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();

            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Room not Available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while Saving a Booking"+e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {

        Response response = new Response();

        try {

            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Booking Not Found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Finding a Booking"+e.getMessage());
        }

        return response;

    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();

        try {

            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while retrieving all the Bookings"+e.getMessage());
        }

        return response;

    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {

            bookingRepository.findById(bookingId).orElseThrow(()-> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a Booking"+e.getMessage());
        }

        return response;
    }


    // Method to check if a room is available for a given booking request
    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        // Stream through the list of existing bookings
        return existingBookings.stream()
                // Check if none of the existing bookings match the given conditions
                .noneMatch(existingBooking ->
                        // Condition 1: Check if the check-in date of the booking request is the same as any existing booking's check-in date
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())

                                // Condition 2: Check if the check-out date of the booking request is before any existing booking's check-out date
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())

                                // Condition 3: Check if the check-in date of the booking request is after the check-in date and before the check-out date of any existing booking
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))

                                // Condition 4: Check if the booking request overlaps with any existing booking
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate()))

                                // Condition 5: Check if the check-in date of the booking request is equal to the check-out date and the check-out date is equal to the check-in date of any existing booking
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                // Condition 6: Check if the check-in date of the booking request is equal to the check-out date and the check-out date is equal to the check-in date of the booking request
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

}
