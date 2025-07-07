package com.sesac7.hellopet.domain.auth.repository;

import com.sesac7.hellopet.domain.auth.entity.RefreshToken;
import com.sesac7.hellopet.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteRefreshTokenByUser(User foundUser);

    RefreshToken findRefreshTokenByToken(String token);

    boolean existsRefreshTokenByUser(User foundUser);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from RefreshToken rt where rt.user.email = :email")
    void deleteRefreshTokenByUser_Email(@Param("email") String email);
}
