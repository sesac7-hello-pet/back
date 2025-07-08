package com.sesac7.hellopet.domain.user.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.common.utils.JwtUtil;
import com.sesac7.hellopet.domain.auth.dto.request.CheckPasswordRequest;
import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import com.sesac7.hellopet.domain.user.dto.request.CheckField;
import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.request.UserSearchRequest;
import com.sesac7.hellopet.domain.user.dto.request.UserUpdateRequest;
import com.sesac7.hellopet.domain.user.dto.response.AdminUserResponse;
import com.sesac7.hellopet.domain.user.dto.response.ExistResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserDetailResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserPageResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserUpdateResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import com.sesac7.hellopet.domain.user.repository.UserDetailRepository;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}$");
    private static final Pattern PHONE_REGEX =
            Pattern.compile("^010-?\\d{4}-?\\d{4}$");
    private static final Pattern KOREAN_REGEX =
            Pattern.compile("^[가-힣]+$");
    private static final Pattern ENGLISH_REGEX =
            Pattern.compile("^[A-Za-z]+$");

    private final UserFinder userFinder;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입시 유저 정보를 가지고 User, UserDetail 엔티티를 저장하고 정보를 반환합니다.
     *
     * @param request 회원가입시 보내는 유저 정보
     * @return 닉네임, 이메일 등
     */
    public UserRegisterResponse userRegister(UserRegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        String profileUrl = StringUtils.hasText(request.getUserProfileUrl())
                ? request.getUserProfileUrl()
                : "https://i.namu.wiki/i/M0j6sykCciGaZJ8yW0CMumUigNAFS8Z-dJA9h_GKYSmqqYSQyqJq8D8xSg3qAz2htlsPQfyHZZMmAbPV-Ml9UA.webp";

        if (doCheck(CheckField.EMAIL, request.getEmail()) && doCheck(CheckField.NICKNAME, request.getNickname())
                && doCheck(CheckField.PHONE, request.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중입니다");
        }

        User user = userRepository.save(request.toDomain(profileUrl));

        return UserRegisterResponse.from(user);
    }

    public LoginResponse userLogin(String username) {
        return userRepository.findByEmailToLoginResponse(username);
    }

    @Transactional(readOnly = true)
    public ExistResponse checkExist(CheckField field, String value) {

        boolean exists = doCheck(field, value);

        String message = exists ? "이미 사용 중입니다." : "사용 가능합니다!";

        return new ExistResponse(field, value, exists, message);
    }

    private boolean doCheck(CheckField field, String value) {
        boolean exists;

        switch (field) {
            case EMAIL -> {
                // 1) 형식 검증
                if (!EMAIL_REGEX.matcher(value).matches()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "유효하지 않은 이메일 형식입니다."
                    );
                }
                // 2) 중복 검사
                exists = userRepository.existsUserByEmail(value);
            }
            case PHONE -> {
                if (!PHONE_REGEX.matcher(value).matches()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "휴대폰 번호는 010-0000-0000 형식이어야 합니다."
                    );
                }
                exists = userDetailRepository.existsUserDetailByPhoneNumber(value);
            }
            case NICKNAME -> {
                if (!KOREAN_REGEX.matcher(value).matches() &&
                        !ENGLISH_REGEX.matcher(value).matches()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "닉네임은 한글 또는 영문자만 사용할 수 있습니다."
                    );
                }
                if (KOREAN_REGEX.matcher(value).matches() && value.length() < 2) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "한글 닉네임은 최소 2자 이상이어야 합니다."
                    );
                }
                if (ENGLISH_REGEX.matcher(value).matches() && value.length() < 5) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "영문 닉네임은 최소 5자 이상이어야 합니다."
                    );
                }
                exists = userDetailRepository.existsUserDetailByNickname(value);
            }
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        }

        // 3) 중복된 값이 있으면 409로 응답
        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 존재하는 " + field + "입니다."
            );
        }

        return false;
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(CustomUserDetails userDetails) {
        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        UserDetail userDetail = user.getUserDetail();

        return UserDetailResponse.from(user, userDetail);
    }

    public UserUpdateResponse updateUserDetail(@Valid UserUpdateRequest request, CustomUserDetails userDetails) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        doCheck(CheckField.NICKNAME, request.getNickname());

        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        UserDetail userDetail = userDetailRepository.findByUser(user);

        request.updateUser(user, userDetail);

        userFinder.deleteUsername(user.getEmail());

        userRepository.save(user);
        userDetailRepository.save(userDetail);

        return UserUpdateResponse.from(userDetail);
    }

    public boolean verifyLoggedInUserPassword(User loggedInUser, @Valid CheckPasswordRequest request) {
        return passwordEncoder.matches(request.getPassword(), loggedInUser.getPassword());
    }

    public List<ResponseCookie> disableUser(CustomUserDetails userDetails) {
        User foundUser = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        foundUser.setActivation(false);
        userRepository.save(foundUser);

        userFinder.deleteUsername(userDetails.getUsername());
        SecurityContextHolder.clearContext();

        ResponseCookie deleteAccess = jwtUtil.deleteAccessCookie();
        ResponseCookie deleteRefresh = jwtUtil.deleteRefreshCookie();

        return new ArrayList<>(List.of(deleteAccess, deleteRefresh));
    }

    public UserPageResponse getUsers(UserSearchRequest request) {

        Page<AdminUserResponse> userPages = userRepository.searchUsersByCondition(request.getUserSearchType().name(),
                request.getKeyword(),
                request.toPageable());

        return UserPageResponse.from(userPages, request);
    }

    public void deactivateUser(Long userId) {
        User foundUser = userRepository.findById(userId)
                                       .orElseThrow(() -> new UsernameNotFoundException("잘못된 USER ID 입니다."));
        if (!foundUser.getActivation()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 비활성화 된 유저입니다.");
        }
        if (foundUser.getRole().equals(UserRole.ADMIN)) {
            throw new AuthorizationDeniedException("권한이 없습니다.");
        }
        foundUser.setActivation(false);
    }

    public User getUserByEmailFromDatabase(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 입니다."));
    }
}
