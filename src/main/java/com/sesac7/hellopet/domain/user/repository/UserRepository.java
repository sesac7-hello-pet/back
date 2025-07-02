package com.sesac7.hellopet.domain.user.repository;

import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import com.sesac7.hellopet.domain.user.dto.request.UserSearchType;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    boolean existsUserByEmail(String value);

    List<User> findByRole(UserRole userRole);

    @Query("""
            select u from User u join u.userDetail ud
                        where (:searchType = com.sesac7.hellopet.domain.user.dto.request.UserSearchType.TOTAL and (u.email like %:keyword% or ud.username like %:keyword% or ud.nickname like %:keyword%)) 
                                    or (:searchType = com.sesac7.hellopet.domain.user.dto.request.UserSearchType.USERNAME and (ud.username like %:keyword%))
                                    or (:searchType = com.sesac7.hellopet.domain.user.dto.request.UserSearchType.EMAIL and (u.email like %:keyword%))
                                    or (:searchType = com.sesac7.hellopet.domain.user.dto.request.UserSearchType.NICKNAME and (ud.nickname like %:keyword%))
            """)
    Page<User> searchUsersByCondition(@Param("searchType") UserSearchType searchType, @Param("keyword") String keyword,
                                      Pageable pageable);
}
