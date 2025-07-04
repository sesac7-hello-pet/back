package com.sesac7.hellopet.common.utils.security;

import com.sesac7.hellopet.domain.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("applicationSecurity")
@RequiredArgsConstructor
public class ApplicationSecurity {
    private final ApplicationRepository applicationRepository;

    public boolean isOwner(Long applicationId, Authentication authentication) {
        return applicationRepository.findById(applicationId)
                                    .map(application -> application.getApplicant().getEmail()
                                                                   .equals(authentication.getName()))
                                    .orElse(false);
    }
}
