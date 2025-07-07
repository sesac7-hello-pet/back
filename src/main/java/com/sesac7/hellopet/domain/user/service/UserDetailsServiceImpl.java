package com.sesac7.hellopet.domain.user.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.global.exception.custom.WithdrawUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserFinder userFinder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userFinder.findLoggedInUserByUsername(email);

        if (!user.getActivation()) {
            throw new WithdrawUserException();
        }

        return new CustomUserDetails(user);
    }
}
