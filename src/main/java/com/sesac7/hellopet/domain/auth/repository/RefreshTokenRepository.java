package com.sesac7.hellopet.domain.auth.repository;

import com.sesac7.hellopet.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsRefreshTokenByToken(String refreshToken);
}
