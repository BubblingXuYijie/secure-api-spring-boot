package icu.xuyijie.secureapi.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:29
 * @description 根据此注解决定是否解密接口param或formData参数，下面的配置项和@RequestParam功能一致
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Documented
public @interface DecryptParam {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default "";
}
