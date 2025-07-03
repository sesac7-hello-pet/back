package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.dto.request.UserSearchRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class UserPageResponse {
    private int page;
    private int size;
    private int totalPages;
    private Long totalCount;
    private List<AdminUserResponse> adminUserList = new ArrayList<>();

    public static UserPageResponse from(Page<AdminUserResponse> page, UserSearchRequest request) {
        return new UserPageResponse(request.getPage(), request.getSize(), page.getTotalPages(),
                page.getTotalElements(), page.getContent());
    }
}
