package com.sesac7.hellopet.domain.announcement.dto.response;

import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementSearchRequest;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class AnnouncementPageResponse {
    private int page;
    private int size;
    private int totalPages;
    private Long totalCount;
    private List<AnnouncementListResponse> announcements;

    public static AnnouncementPageResponse from( Page<AnnouncementListResponse> announcements, AnnouncementSearchRequest request) {
        return new AnnouncementPageResponse(
                request.getPage(), request.getSize(),announcements.getTotalPages(), announcements.getTotalElements(), announcements.getContent()
        );

    }
}
