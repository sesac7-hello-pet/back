package com.sesac7.hellopet.domain.announcement.dto.response;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AnnouncementList {
    private String breed;
    private String image;
    private boolean status;
    private Long id;

    public static AnnouncementList from(Announcement announcement) {
        return new AnnouncementList(
                announcement.getPet().getBreed(),        // pet에서 breed 가져오기
                announcement.getImageUrl(),               // announcement의 imageUrl 사용
                announcement.getStatus() == AnnouncementStatus.ACTIVE, // enum 상태 변환 (예시)
                announcement.getId()
        );
    }
}
