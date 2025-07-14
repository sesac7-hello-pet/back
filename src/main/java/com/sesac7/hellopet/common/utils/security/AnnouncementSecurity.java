package com.sesac7.hellopet.common.utils.security;

import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("announcementSecurity")
@RequiredArgsConstructor
public class AnnouncementSecurity {
    private final AnnouncementRepository announcementRepository;

    /**
     * 커스텀 어노테이션 사용을 위한 클래스입니다.
     * 위에 Component 어노테이션으로 이름을 지정해놨습니다.
     * isOwner 라는 메서드를 구현해서
     * announcementId로 announcement를 찾고 찾은 엔티티의 작성자가 현재 로그인 된 작성자인지 확인합니다.
     *
     * @param announcementId
     * @param authentication
     * @return
     */
    public boolean isOwner(Long announcementId, Authentication authentication) {
        return announcementRepository.findById(announcementId)
                                     .map(announcement -> announcement.getShelter().getEmail()
                                                                      .equals(authentication.getName()))
                                     .orElse(false);
    }
}
