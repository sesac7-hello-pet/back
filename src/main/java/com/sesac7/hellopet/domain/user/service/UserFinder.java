package com.sesac7.hellopet.domain.user.service;

import com.sesac7.hellopet.domain.user.entity.User;

public interface UserFinder {
    /**
     * 로그인된 유저를 이메일로 가져올때 사용하시면 됩니다.
     *
     * @param email
     * @return User
     */
    User findLoggedInUserByUsername(String email);

    void deleteUsername(String email);
}
