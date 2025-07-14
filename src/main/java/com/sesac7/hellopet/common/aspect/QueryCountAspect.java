package com.sesac7.hellopet.common.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class QueryCountAspect {

    private final SessionFactory sessionFactory;

    /**
     * SQL의 갯수를 로깅하는 메서드입니다.
     *
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sesac7.hellopet.domain..service..*(..))")
    public Object logSqlCount(ProceedingJoinPoint jp) throws Throwable {
        Long before = sessionFactory.getStatistics().getPrepareStatementCount();

        try {
            return jp.proceed();
        } finally {
            Long after = sessionFactory.getStatistics().getPrepareStatementCount();
            Long delta = after - before;

            log.info("🔸 {} → SQL {} Execute", jp.getSignature().getName(), delta);
        }
    }
}
