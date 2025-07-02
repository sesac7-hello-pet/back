package com.sesac7.hellopet.domain.user.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationPageRequest;
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationPageResponse;
import com.sesac7.hellopet.domain.application.service.ApplicationService;
import com.sesac7.hellopet.domain.board.dto.response.BoardPageResponse;
import com.sesac7.hellopet.domain.board.service.BoardService;
import com.sesac7.hellopet.domain.user.dto.request.UserUpdateRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserDetailResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserUpdateResponse;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import com.sesac7.hellopet.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final BoardService boardService;
    private final UserFinder userFinder;

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
        List<ResponseCookie> responseCookies = userService.disableUser(userDetails);
        return ResponseEntity.noContent()
                             .header(HttpHeaders.SET_COOKIE, responseCookies.get(0).toString())
                             .header(HttpHeaders.SET_COOKIE, responseCookies.get(1).toString())
                             .build();
    }

    @GetMapping("/applications")
    public ResponseEntity<UserApplicationPageResponse> getApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute @Valid ApplicationPageRequest request) {

        UserApplicationPageResponse response = applicationService.getUserApplications(userDetails, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/boards")
    public ResponseEntity<BoardPageResponse> getBoards(@AuthenticationPrincipal CustomUserDetails details,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(boardService.getMyBoard(details, page, size));
    }
}
