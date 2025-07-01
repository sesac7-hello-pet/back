package com.sesac7.hellopet.domain.announcement.dto.response;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AnnouncementListResponse {
    private String breed;
    private String image;
    private boolean status;
    private Long id;

    public static AnnouncementListResponse from(Announcement announcement, Pet pet) {
        return new AnnouncementListResponse(
                announcement.getPet().getBreed(),        // pet에서 breed 가져오기
                announcement.getPet().getImageUrl(),               // announcement의 imageUrl 사용

                announcement.getStatus() == AnnouncementStatus.ACTIVE, // enum 상태 변환 (예시)
                announcement.getId()
        );
    }
}
