package com.sesac7.hellopet.domain.application.dto.response;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class ShelterApplicationsPageResponse {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private Long announcementId;
    private LocalDateTime announcementCreatedAt;
    private List<ShelterApplicationResponse> applications;

    public static ShelterApplicationsPageResponse of(Pageable pageable
            , List<ShelterApplicationResponse> content
            , long totalElements
            , Announcement announcement) {

        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());

        return ShelterApplicationsPageResponse.builder()
                                              .page(pageable.getPageNumber())
                                              .size(pageable.getPageSize())
                                              .totalElements(totalElements)
                                              .totalPages(totalPages)
                                              .announcementId(announcement.getId())
                                              .announcementCreatedAt(announcement.getCreatedAt())
                                              .applications(content)
                                              .build();
    }
}
