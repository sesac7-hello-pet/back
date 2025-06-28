package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.domain.user.Service.UserService;
import com.sesac7.hellopet.domain.user.dto.request.CheckField;
import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.userRegister(request));
    }

    @GetMapping("/exist")
    public ResponseEntity<ExistResponse> check(@RequestParam CheckField field,
                                               @RequestParam String value) {
        return null;
    }
}
