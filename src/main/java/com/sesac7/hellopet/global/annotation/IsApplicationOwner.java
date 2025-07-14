package com.sesac7.hellopet.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * common/utils/security에 있던 메서드들을 활용하기 위한 어노테이션 입니다.
 * target은 적용될 범위를 나타내고
 * retention은 수명을 나타냅니다
 * preAuthorize는 언제 적용될지를 나타냅니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@applicationSecurity.isOwner(#applicationId, authentication)")
public @interface IsApplicationOwner {
}
