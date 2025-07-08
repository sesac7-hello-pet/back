package com.sesac7.hellopet.common.utils;

import org.springframework.stereotype.Component;

@Component
public interface DataSetter {
    void userGenerator(int num);
    void announcementGenerator(int num);
    void boardGenerator(int num);
    void commentGenerator(int num);
    void applicationGenerator(int num);
}
