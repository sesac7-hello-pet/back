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

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

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
            case EMAIL    -> exists = userRepository.existsUserByEmail(value);
            case PHONE    -> exists = userDetailRepository.existsUserDetailByPhoneNumber(value);
            case NICKNAME -> exists = userDetailRepository.existsUserDetailByNickname(value);
            default       -> throw new IllegalArgumentException("Unknown field");
        }
        return exists;
    }
}
