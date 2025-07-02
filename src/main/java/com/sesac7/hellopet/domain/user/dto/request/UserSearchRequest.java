package com.sesac7.hellopet.domain.user.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class UserSearchRequest {

    @Enumerated(EnumType.STRING)
    private UserSortType userSortType;

    @Enumerated(EnumType.STRING)
    private UserAscDesc userAscDesc;

    @Enumerated(EnumType.STRING)
    private UserSearchType userSearchType;

    private String keyword;

    private Integer requestPage;
    private Integer requestSize;

    public Pageable toPageable() {

        int page = requestPage != null ? requestPage : 0;
        int size = requestSize != null ? requestSize : 10;

        Sort.Direction direction =
                (userAscDesc != null ? userAscDesc : UserAscDesc.ASC) == UserAscDesc.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        String sortProperty;
        switch (userSortType != null ? userSortType : UserSortType.ID) {
            case ROLE:
                sortProperty = "role";
                break;
            case USERNAME:
                sortProperty = "username";
                break;
            case EMAIL:
                sortProperty = "email";
                break;
            case NICKNAME:
                sortProperty = "nickname";
                break;
            case PHONENUMBER:
                sortProperty = "phoneNumber";
                break;
            case ID:
            default:
                sortProperty = "id";
        }

        return PageRequest.of(page, size, Sort.by(direction, sortProperty));
    }
}
