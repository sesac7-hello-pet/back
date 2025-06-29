package com.sesac7.hellopet.domain.user.Service;

import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import com.sesac7.hellopet.domain.user.dto.request.CheckField;
import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.response.ExistResponse;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.repository.UserDetailRepository;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

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

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입시 유저 정보를 가지고 User, UserDetail 엔티티를 저장하고 정보를 반환합니다.
     *
     * @param request 회원가입시 보내는 유저 정보
     * @return 닉네임, 이메일 등
     */
    public UserRegisterResponse userRegister(UserRegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        String nickname = StringUtils.hasText(request.getNickname())
                ? request.getNickname()
                : request.getUsername() + Math.floor(Math.random() * (99999 - 1000 + 1)) + 1000; // 닉네임이 안겹치도록
        String profileUrl = StringUtils.hasText(request.getUserProfileUrl())
                ? request.getUserProfileUrl()
                : "https://i.namu.wiki/i/M0j6sykCciGaZJ8yW0CMumUigNAFS8Z-dJA9h_GKYSmqqYSQyqJq8D8xSg3qAz2htlsPQfyHZZMmAbPV-Ml9UA.webp";

        if(doCheck(CheckField.EMAIL, request.getEmail()) && doCheck(CheckField.NICKNAME, request.getNickname()) && doCheck(CheckField.PHONE, request.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중입니다");
        }

        User user = userRepository.save(request.toDomain(nickname, profileUrl));

        return UserRegisterResponse.from(user);
    }

    public LoginResponse userLogin(String username) {
        return userRepository.findByEmailToLoginResponse(username);
    }

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
}
