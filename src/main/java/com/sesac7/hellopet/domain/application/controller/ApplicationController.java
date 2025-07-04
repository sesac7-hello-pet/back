package com.sesac7.hellopet.domain.application.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationCreateRequest;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.ApplicationDetailResponse;
import com.sesac7.hellopet.domain.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/applications/{id}")
    public ResponseEntity<ApplicationDetailResponse> getApplication(@PathVariable Long id) {
        ApplicationDetailResponse response = applicationService.getApplication(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationCreateRequest request,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApplicationResponse response = applicationService.createApplication(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        applicationService.deleteApplication(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}
