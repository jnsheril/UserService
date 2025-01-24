package com.scaler.userservice.controllers;

import com.scaler.userservice.dtos.*;
import com.scaler.userservice.dtos.ResponseStatus;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.repositories.TokenRepository;
import com.scaler.userservice.repositories.UserRepository;
import com.scaler.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


// to treat this class as special class when JVM run
@RestController
//root mapping
@RequestMapping("/users")

// here we define different methods that we want to support
// signUp, login, validate token,logout


public class UserController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private UserService userService;
    public UserController(UserService userService, UserRepository userRepository, TokenRepository tokenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }
    @PostMapping("/login")
    public LoginResponseDto login (@RequestBody LoginRequestDto requestDto){
      // return type of login method is Token
        Token token = userService.login(requestDto.getEmail(), requestDto.getPassword());
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(token);
        return responseDto;
    }

    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto requestDto){
        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword());

        SignUpResponseDto responseDto = new SignUpResponseDto();
        responseDto.setUser(user);
        responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        return responseDto;
    }
@GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable("token") String token){
        User user = userService.validateToken(token);

        return UserDto.fromUser(user);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto) {
        ResponseEntity<Void> responseEntity = null;
        try {
            userService.logout(requestDto.getToken());
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
}


