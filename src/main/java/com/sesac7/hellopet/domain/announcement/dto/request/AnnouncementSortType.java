package com.sesac7.hellopet.domain.announcement.dto.request;

public enum AnnouncementSortType {
    CREATEDAT, ID,SHELTER;

    public static AnnouncementSortType getByName(String name) {
        try {
            return AnnouncementSortType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return CREATEDAT;
        }
    }
}
