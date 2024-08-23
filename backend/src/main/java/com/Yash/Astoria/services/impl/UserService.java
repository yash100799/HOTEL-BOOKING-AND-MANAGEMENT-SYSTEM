package com.Yash.Astoria.services.impl;

import com.Yash.Astoria.dto.LoginRequest;
import com.Yash.Astoria.dto.Response;
import com.Yash.Astoria.dto.UserDTO;
import com.Yash.Astoria.entities.User;
import com.Yash.Astoria.exception.OurException;
import com.Yash.Astoria.repository.UserRepository;
import com.Yash.Astoria.services.Interface.IUserService;
import com.Yash.Astoria.utils.JWTUtils;
import com.Yash.Astoria.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {

        Response response = new Response();

        try {

            //By Default user registration role will be set as USER
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            //Since we need each user to use a unique email id for registration
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + "Already Exists");
            }

            //encoding the given password
            //Saving user data into repo
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);

            //for data related errors
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
            //for other exceptions
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {

            //Login - authenticating using password and email
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User Not Found"));

            //generate token if user found
            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successful");

            //for data related errors
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            //for other exceptions
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {

            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();

        try {

            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

            //for data related errors
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            //for other exceptions
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred while fetching all Users" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {

        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {

        Response response = new Response();

        try {

            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

            //for data related errors
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            //for other exceptions
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred while Deleting the User" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {

        Response response = new Response();

        try {

            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

            //for data related errors
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            //for other exceptions
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred while retrieving the User Info" + e.getMessage());
        }

        return response;
    }
}
