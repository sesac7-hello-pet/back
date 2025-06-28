package com.sesac7.hellopet.common.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final DataSetter dataSetter;

    @PostConstruct
    public void userGenerator() {
        dataSetter.userGenerator(10);
    }

    @PostConstruct
    public void announcementGenerator() {
        dataSetter.announcementGenerator(10);
    }
}
