package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<UserRegisterResponse> register(UserRegisterRequest request) {
        return null;
    }
}
