package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.domain.user.dto.request.CheckField;
import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.request.UserSearchRequest;
import com.sesac7.hellopet.domain.user.dto.response.AdminUserListResponse;
import com.sesac7.hellopet.domain.user.dto.response.ExistResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import com.sesac7.hellopet.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<AdminUserListResponse> getUsers(@RequestBody UserSearchRequest request) {
        return userService.getUsers(request);
    }

}
