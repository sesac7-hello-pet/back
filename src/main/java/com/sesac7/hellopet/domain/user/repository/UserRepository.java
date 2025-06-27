package com.sesac7.hellopet.domain.user.repository;

import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
                select new com.sesac7.hellopet.domain.auth.dto.response.LoginResponse(
                    u.id,
                    u.email,
                    u.role,
                    ud.nickname,
                    ud.userProfileUrl
                )
                from User u
                join u.userDetail ud
                where u.email = :email
            """)
    LoginResponse findByEmailToLoginResponse(@Param("email") String email);

}
