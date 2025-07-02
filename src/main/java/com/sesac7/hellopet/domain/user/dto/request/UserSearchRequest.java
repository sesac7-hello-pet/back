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
        // 1) 페이징 기본값
        int page = requestPage != null ? requestPage : 0;
        int size = requestSize != null ? requestSize : 10;

        // 2) 정렬 방향: 문자열→enum→Sort.Direction
        UserAscDesc ascDescEnum =
                UserAscDesc.toEnum(userAscDesc != null ? userAscDesc.name() : null);
        Sort.Direction direction =
                ascDescEnum == UserAscDesc.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        // 3) 정렬 대상 프로퍼티: 문자열→enum→필드명 매핑
        UserSortType sortTypeEnum =
                UserSortType.toEnum(userSortType != null ? userSortType.name() : null);

        String sortProperty;
        switch (sortTypeEnum) {
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

        // 4) PageRequest 생성
        return PageRequest.of(page, size, Sort.by(direction, sortProperty));
    }
}
