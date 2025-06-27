package com.sesac7.hellopet.domain.user.Service;

import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
                : request.getUsername();
        String profileUrl = StringUtils.hasText(request.getUserProfileUrl())
                ? request.getUserProfileUrl()
                : "https://i.namu.wiki/i/M0j6sykCciGaZJ8yW0CMumUigNAFS8Z-dJA9h_GKYSmqqYSQyqJq8D8xSg3qAz2htlsPQfyHZZMmAbPV-Ml9UA.webp";

        User user = userRepository.save(request.toDomain(nickname, profileUrl));

        return UserRegisterResponse.from(user);
    }
}
