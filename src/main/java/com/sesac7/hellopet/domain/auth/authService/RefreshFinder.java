package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.domain.user.entity.User;

public interface RefreshFinder {
    Boolean existRefresh(String refreshToken);

    void deleteRefreshByUser(User foundUser);
}
