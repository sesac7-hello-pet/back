package com.sesac7.hellopet.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class PerformanceMonitoringAspect {

    @Around("execution(* com.sesac7.hellopet.domain..service..*(..))")
    public Object monitorObjectExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        Long startTime = System.currentTimeMillis();
        try {
            return jp.proceed();
        } finally {
            Long endTime = System.currentTimeMillis();
            log.info("🧭  ExecutionTime: {} ⇒ {}ms", jp.getSignature().getName(), endTime - startTime);
        }
    }
}
