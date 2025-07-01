package com.sesac7.hellopet.domain.user.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import com.sesac7.hellopet.global.exception.custom.WithdrawUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail((email))
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 Email 입니다. "));

        if (!user.getActivation()) {
            throw new WithdrawUserException();
        }

        return new CustomUserDetails(user);
    }
}
