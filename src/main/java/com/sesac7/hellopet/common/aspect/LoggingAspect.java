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
    /**
     * pointcut은 어느범위의 메서드를 로깅 할 것인가를 정합니다
     * 보통 아무 동작없는 메서드에 포인트 컷을 걸고
     * 밑에 before이나 after에서 적용하여 사용하는 용도 입니다.
     */
    @Pointcut("execution(* com.sesac7.hellopet.domain..*(..))")
    private void anyMethod() {
    }

    /**
     * Before은 포인트컷 범위 내의 메서드가 실행되기 전에 실행될 내용입니다.
     *
     * @param jp
     */
    @Before("anyMethod()")
    public void beforeAdvice(JoinPoint jp) {
        log.info("▶️  Before: {}", jp.getSignature().getName());
    }

    /**
     * afterReturning은 포인트컷 범위 내의 메서드가 실행되고 리턴 된 후 실행될 내용입니다.
     *
     * @param jp
     * @param result
     */
    @AfterReturning(pointcut = "anyMethod()", returning = "result")
    public void afterReturningAdvice(JoinPoint jp, Object result) {
        log.info("✅  AfterReturning: {} ⇒ {}", jp.getSignature().getName(), result);
    }
}
