package com.zomato.app.controller;

import com.zomato.app.dto.LoginRequest;
import com.zomato.app.dto.LoginResponse;
import com.zomato.app.dto.RefreshTokenRequest;
import com.zomato.app.dto.RegisterRequest;
import com.zomato.app.service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service){

        this.service = service;

    }

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request){

        return service.register(request);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody RefreshTokenRequest request) {
        return service.refresh(request);
    }

}
