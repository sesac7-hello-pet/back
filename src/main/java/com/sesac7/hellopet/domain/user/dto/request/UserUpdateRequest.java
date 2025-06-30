package com.sesac7.hellopet.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserUpdateRequest {

    @Setter
    @Getter
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 각각 최소 한 개씩 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "닉네임은 필수입니다")
    @Pattern(
            regexp = "^[가-힣]{2,}$|^[A-Za-z]{5,}$",
            message = "닉네임은 한글 2자 이상 또는 영문 5자 이상이어야 합니다."
    )
    private String nickname;

    @NotBlank(message = "주소는 필수입니다")
    private String address;

    private String userProfileUrl;
}
