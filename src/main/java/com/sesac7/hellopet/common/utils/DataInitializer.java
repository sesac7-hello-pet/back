package com.sesac7.hellopet.common.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final DataSetter dataSetter;

    private static final int HOW_MANY_MAKE_DATA = 30;

    @PostConstruct
    public void initAllData() {
//        dataSetter.userGenerator(HOW_MANY_MAKE_DATA);
//        dataSetter.announcementGenerator(HOW_MANY_MAKE_DATA);
//        dataSetter.boardGenerator(HOW_MANY_MAKE_DATA);
//        dataSetter.commentGenerator(HOW_MANY_MAKE_DATA);
    }
}
