package icu.xuyijie.secureapi.annotation;

import java.lang.annotation.*;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:29
 * @description 根据此注解决定是否解密接口参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface DecryptApi {
}
