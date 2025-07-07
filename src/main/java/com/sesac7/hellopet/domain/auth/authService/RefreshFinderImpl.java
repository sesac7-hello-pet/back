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

    @Override
    public void deleteRefreshByUser(User foundUser) {
        refreshTokenRepository.deleteRefreshTokenByUser(foundUser);
    }

    @Override
    public User getUserByToken(String token) throws NullPointerException {
        return refreshTokenRepository.findRefreshTokenByToken(token).getUser();
    }

    @Override
    public boolean existRefreshByUser(User foundUser) {
        return refreshTokenRepository.existsRefreshTokenByUser(foundUser);
    }

    @Override
    public void deleteRefreshByEmail(String email) {
        refreshTokenRepository.deleteRefreshTokenByUser_Email(email);
    }

    @Override
    public void saveRefresh(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

}
