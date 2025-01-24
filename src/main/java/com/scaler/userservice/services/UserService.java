package com.scaler.userservice.services;

import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;

public interface UserService {

    User signUp(String name, String email, String password);

    Token login(String email, String password);

    User validateToken(String token);

    void logout(String token);
}
