package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.domain.user.dto.request.UserSearchRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserPageResponse;
import com.sesac7.hellopet.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserPageResponse> getUsers(@ModelAttribute UserSearchRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
