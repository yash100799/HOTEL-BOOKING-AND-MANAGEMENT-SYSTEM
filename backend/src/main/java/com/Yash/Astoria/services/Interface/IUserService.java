package com.Yash.Astoria.services.Interface;

import com.Yash.Astoria.dto.LoginRequest;
import com.Yash.Astoria.dto.Response;
import com.Yash.Astoria.entities.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
