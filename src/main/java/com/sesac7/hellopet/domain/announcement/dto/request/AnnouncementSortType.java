package com.sesac7.hellopet.domain.announcement.dto.request;

public enum AnnouncementSearchType {
    CREATEDAT, ID,SHELTER;

    public static AnnouncementSearchType getByName(String name) {
        try {
            return AnnouncementSearchType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return CREATEDAT;
        }
    }
}
