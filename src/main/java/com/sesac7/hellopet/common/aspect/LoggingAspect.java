package com.sesac7.hellopet.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.sesac7.hellopet.domain..*(..))")
    private void anyMethod() {
    }

    @Before("anyMethod()")
    public void beforeAdvice(JoinPoint jp) {
        log.info("▶️  Before: {}", jp.getSignature().getName());
    }

    @AfterReturning(pointcut = "anyMethod()", returning = "result")
    public void afterReturningAdvice(JoinPoint jp, Object result) {
        log.info("✅  AfterReturning: {} ⇒ {}", jp.getSignature().getName(), result);
    }
}
