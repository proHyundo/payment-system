package pro.hyundo.paymentsystem.global.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 웹 어댑터임을 나타내는 커스텀 어노테이션입니다.
 * 이 어노테이션이 붙은 클래스는 Spring의 Component로 스캔됩니다.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface WebAdapter {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
