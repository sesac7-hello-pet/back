package com.sesac7.hellopet.common.utils;

import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetails에서 필드를 더 활용하려고 만든 CustomUserDetails입니다.
 * 보통 spring에서 미리 생성되어있는 객체에 다른 요소를 추가하고 싶다면
 * 이렇게 구현하여 사용합니다.
 * 근데 삭제할거라서 몰라도 됩니다.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;        // 이메일
    private final String password;        // BCrypt 해시
    private final UserRole role;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /* ── UserDetails 필수 메서드 ───────────────────────────── */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
