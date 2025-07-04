package com.sesac7.hellopet.common.utils.security;

import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("announcementSecurity")
@RequiredArgsConstructor
public class AnnouncementSecurity {
    private final AnnouncementRepository announcementRepository;

    public boolean isOwner(Long announcementId, Authentication authentication) {
        return announcementRepository.findById(announcementId)
                                     .map(announcement -> announcement.getShelter().getEmail()
                                                                      .equals(authentication.getName()))
                                     .orElse(false);
    }
}
