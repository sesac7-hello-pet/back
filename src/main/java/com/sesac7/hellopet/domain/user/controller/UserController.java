package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.domain.user.dto.response.AdminUserListResponse;
import com.sesac7.hellopet.domain.user.service.UserService;
import com.sesac7.hellopet.domain.user.dto.request.CheckField;
import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.response.ExistResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkExist(field, value));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AdminUserListResponse> getUsers() {
        return userService.getUsers();
    }

}
