package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshFinderImpl implements RefreshFinder {
    private final RefreshTokenRepository refreshTokenRepository;

    public Boolean existRefresh(String refreshToken) {
        return refreshTokenRepository.existsRefreshTokenByToken(refreshToken);
    }
}
