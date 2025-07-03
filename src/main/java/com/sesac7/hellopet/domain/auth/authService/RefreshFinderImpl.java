package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.domain.auth.entity.RefreshToken;
import com.sesac7.hellopet.domain.auth.repository.RefreshTokenRepository;
import com.sesac7.hellopet.domain.user.entity.User;
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

    @Override
    public void deleteRefreshByUser(User foundUser) {
        refreshTokenRepository.deleteRefreshTokenByUser(foundUser);
    }

    @Override
    public User getUserByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByToken(token);
        return refreshToken.getUser();
    }
}
