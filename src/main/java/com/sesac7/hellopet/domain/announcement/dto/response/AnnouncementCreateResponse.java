package com.sesac7.hellopet.domain.announcement.dto.response;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementCreateResponse {
    private Long id;
    private String announcementId;
    private String shelterName;
    private String message;
    /**
     * 공고 ID 및 정보 반환
     * @param announcement
     * @return
     */
    public static AnnouncementCreateResponse from(Announcement announcement) {
        return new AnnouncementCreateResponse(
                announcement.getId(),
                announcement.getShelter() + "-" + announcement.getCreateAt() + "-" + announcement.getId(),
                announcement.getShelter().getUserDetail().getNickname(), "입양 공고 등록이 완료 되었습니다."
        );
    }
}
