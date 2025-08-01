package com.sesac7.hellopet.domain.user.repository;

import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import com.sesac7.hellopet.domain.user.dto.response.AdminUserResponse;
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
            select new com.sesac7.hellopet.domain.user.dto.response.AdminUserResponse(
                        u.id,
                                    u.email,
                                                u.role,
                                                            ud.nickname,
                                                                        ud.username,
                                                                                    ud.phoneNumber,
                                                                                                u.activation
                        )
            from   User u
            join   u.userDetail ud
            where  ( :searchType = 'TOTAL'
                       and ( u.email    like concat('%', :keyword, '%')
                          or ud.username like concat('%', :keyword, '%')
                          or ud.nickname like concat('%', :keyword, '%') ) )
            
               or  ( :searchType = 'USERNAME'
                       and ud.username like concat('%', :keyword, '%') )
            
               or  ( :searchType = 'EMAIL'
                       and u.email like concat('%', :keyword, '%') )
            
               or  ( :searchType = 'NICKNAME'
                       and ud.nickname like concat('%', :keyword, '%') )
            """)
    Page<AdminUserResponse> searchUsersByCondition(@Param("searchType") String searchType,
                                                   @Param("keyword") String keyword,
                                                   Pageable pageable);

}
