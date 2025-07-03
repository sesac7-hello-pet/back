package com.sesac7.hellopet.domain.announcement.dto.request;

public enum AnnouncementAscDescType {
    ASC, DESC;

    public static AnnouncementAscDescType getByName(String name) {
        try {
            return AnnouncementAscDescType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return DESC;
        }
    }
}
