package com.sesac7.hellopet.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ExceptionLoggingAspect {

    /**
     * 밑에 point cut 안에 있는 범위의 메서드에서 발생하는 예외를 로깅해주는 메서드 입니다.
     *
     * @param jp
     * @param ex
     */
    @AfterThrowing(pointcut = "execution(* com.sesac7.hellopet.domain..*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Throwable ex) {
        log.error("🔥  Exception: {}", jp.getSignature().getName() + ": ", ex);
    }
}
