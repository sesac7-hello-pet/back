package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationResponse;
import com.sesac7.hellopet.domain.application.service.ApplicationService;
import com.sesac7.hellopet.domain.user.dto.request.UserUpdateRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserDetailResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserUpdateResponse;
import com.sesac7.hellopet.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<UserDetailResponse> getDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetail(userDetails));
    }

    @PutMapping
    public ResponseEntity<UserUpdateResponse> updateDetail(@Valid @RequestBody UserUpdateRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserDetail(request, userDetails));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return null;
    }

    @GetMapping("/applications")
    public ResponseEntity<Page<UserApplicationResponse>> getApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute Pageable pageable) { // 프론트에서 ?page=0&size=10와 같이 호출하면 Pageable로 매핑됨

        Page<UserApplicationResponse> response = applicationService.getApplications(userDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
