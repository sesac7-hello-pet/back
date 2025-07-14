package com.sesac7.hellopet.common.utils;

import org.springframework.stereotype.Component;

/**
 * 메서드를 사용하기 편하게 만들기 위하여
 * interface로 분리하였습니다.
 */
@Component
public interface DataSetter {
    void userGenerator(int num);

    void announcementGenerator(int num);

    void boardGenerator(int num);

    void commentGenerator(int num);

    void applicationGenerator(int num);
}
