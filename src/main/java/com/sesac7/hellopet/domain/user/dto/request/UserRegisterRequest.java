package com.sesac7.hellopet.domain.user.dto.request;

import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserRegisterRequest {

    @NotBlank(message = "사용자 이름은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String email;

    @Setter
    @Getter
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 각각 최소 한 개씩 포함해야 합니다."
    )
    private String password;

    @NotNull(message = "사용자 권한은 필수입니다")
    private UserRole role;

    private String nickname;

    @NotBlank(message = "사용자 이름은 필수입니다")
    @Size(min = 2, max = 10, message = "사용자 이름은 2-10자 사이여야 합니다")
    private String username;

    @NotBlank(message = "주소는 필수입니다")
    private String address;

    @NotBlank(message = "사용자 전화번호는 필수입니다")
    @Pattern(
            regexp = "^\\d{11}$",
            message = "휴대폰 번호는 숫자 11자리여야 합니다."
    )
    private String phoneNumber;
    private String userProfileUrl;

    public User toDomain() {
        User user = new User(null, email, password, role, null);
        UserDetail userDetail = new UserDetail(null, nickname, username, userProfileUrl, address,
                Integer.valueOf(phoneNumber), user);
        user.setUserDetail(userDetail);
        return user;
    }
}
