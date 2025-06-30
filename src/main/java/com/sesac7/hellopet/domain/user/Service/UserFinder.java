package com.sesac7.hellopet.domain.user.Service;

import com.sesac7.hellopet.domain.user.entity.User;

public interface UserFinder {
    User findUserByUsername(String email);
}
