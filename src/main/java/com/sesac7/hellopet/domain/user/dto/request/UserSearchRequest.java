package com.sesac7.hellopet.domain.user.dto.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class UserSearchRequest {

    private UserSearchType userSearchType = UserSearchType.TOTAL;
    private UserSortType userSortType = UserSortType.ID;
    private UserAscDesc userAscDesc = UserAscDesc.ASC;

    private String keyword = "";

    private Integer page = 0;
    private Integer size = 10;

    public Pageable toPageable() {
        // 2) 정렬 방향: 문자열→enum→Sort.Direction
        UserAscDesc ascDescEnum =
                UserAscDesc.toEnum(String.valueOf(userAscDesc));
        Sort.Direction direction =
                ascDescEnum == UserAscDesc.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        // 3) 정렬 대상 프로퍼티: 문자열→enum→필드명 매핑
        UserSortType sortTypeEnum =
                UserSortType.toEnum(String.valueOf(userSortType));

        String sortProperty;
        switch (sortTypeEnum) {
            case ROLE:
                sortProperty = "role";
                break;
            case USERNAME:
                sortProperty = "userDetail.username";
                break;
            case EMAIL:
                sortProperty = "email";
                break;
            case NICKNAME:
                sortProperty = "userDetail.nickname";
                break;
            case PHONENUMBER:
                sortProperty = "userDetail.phoneNumber";
                break;
            case ID:
            default:
                sortProperty = "id";
        }

        // 4) PageRequest 생성
        return PageRequest.of(page, size, Sort.by(direction, sortProperty));
    }
}
