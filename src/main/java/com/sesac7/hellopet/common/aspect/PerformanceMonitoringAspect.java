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

    /**
     * 같은 폴더 내의 LoggingAspect에서는 Pointcut, before, after로 나뉘어져 있지만
     * 이곳에는 Aroud로 한꺼번에 감싸져 있습니다.
     * 프록시객체인 joinPoint가 시작되기 전에 startTime을 재고
     * finally로 끝나면 endTime을 재서
     * 타겟 메서드가 실행된 시간을 구합니다.
     *
     * @param jp
     * @return
     * @throws Throwable
     */
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
