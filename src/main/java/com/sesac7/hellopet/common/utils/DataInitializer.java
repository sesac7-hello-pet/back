package com.sesac7.hellopet.common.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final DataSetter dataSetter;

    private static final int HOW_MANY_MAKE_DATA = 100;

    /**
     * PostConstruct로 클래스가 생겨나면 바로 실행되게 만들었습니다.
     * 클래스는 Component 어노테이션이 적용되어 있기 때문에
     * 스프링이 실행되자 마자 컨테이너에 만들어지고
     * 그 이후 바로 initAllData가 실행됩니다.
     * <p>
     * 데이터 생성과 갯수 설정을 한번에 하기 위하여 이렇게 만들었습니다.
     */
    @PostConstruct
    public void initAllData() {
        dataSetter.userGenerator(HOW_MANY_MAKE_DATA);
        dataSetter.announcementGenerator(HOW_MANY_MAKE_DATA);
        dataSetter.boardGenerator(HOW_MANY_MAKE_DATA);
        dataSetter.commentGenerator(HOW_MANY_MAKE_DATA * 3);
        dataSetter.applicationGenerator(HOW_MANY_MAKE_DATA);
    }
}
