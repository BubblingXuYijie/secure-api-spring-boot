package icu.xuyijie.secureapi.annotation;

import java.lang.annotation.*;

/**
 * @author 徐一杰
 * @date 2025/4/10 16:29
 * @description 根据此注解决定是否加密接口返回值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Documented
public @interface EncryptIgnore {
}
