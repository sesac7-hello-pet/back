package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.domain.user.entity.User;

public interface RefreshFinder {
    void deleteRefreshByUser(User foundUser);

    User getUserByToken(String token);

    boolean existRefreshByUser(User foundUser);

    void deleteRefreshByEmail(String email);
}
