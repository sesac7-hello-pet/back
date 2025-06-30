package com.sesac7.hellopet.domain.user.repository;

import com.sesac7.hellopet.domain.user.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    boolean existsUserDetailByPhoneNumber(String value);

    boolean existsUserDetailByNickname(String value);
}
