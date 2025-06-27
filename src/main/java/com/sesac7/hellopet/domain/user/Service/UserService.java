package com.sesac7.hellopet.domain.user.Service;

import com.sesac7.hellopet.domain.user.dto.request.UserRegisterRequest;
import com.sesac7.hellopet.domain.user.dto.response.UserRegisterResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponse userRegister(UserRegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userRepository.save(request.toDomain());
        return UserRegisterResponse.from(user);
    }
}
