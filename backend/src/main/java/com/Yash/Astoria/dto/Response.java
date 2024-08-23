package com.Yash.Astoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

//this DTO is used to handle all the responses from controllers
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;

    private UserDTO user;
    private BookingDTO booking;
    private RoomDTO room;
    private List<UserDTO> userList;
    private  List<BookingDTO> bookingList;
    private List<RoomDTO> roomList;
}
