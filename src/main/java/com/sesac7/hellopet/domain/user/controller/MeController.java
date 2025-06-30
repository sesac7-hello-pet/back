package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Void> getDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return null;
    }
}
