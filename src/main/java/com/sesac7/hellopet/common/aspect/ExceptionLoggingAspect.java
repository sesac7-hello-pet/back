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

    @AfterThrowing(pointcut = "execution(* com.sesac7.hellopet.domain..*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Throwable ex) {
        log.error("🔥  Exception: {}", jp.getSignature().getName() + ": ", ex);
    }
}
