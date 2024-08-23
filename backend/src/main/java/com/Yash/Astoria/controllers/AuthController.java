package com.Yash.Astoria.controllers;

import com.Yash.Astoria.dto.LoginRequest;
import com.Yash.Astoria.dto.Response;
import com.Yash.Astoria.entities.User;
import com.Yash.Astoria.services.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){

        Response response = userService.login(loginRequest);
        return  ResponseEntity.status(response.getStatusCode()).body(response);

    }
}
