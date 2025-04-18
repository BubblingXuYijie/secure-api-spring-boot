package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.annotation.DecryptApi;
import icu.xuyijie.secureapi.annotation.DecryptIgnore;
import icu.xuyijie.secureapi.cipher.utils.RsaSignatureUtils;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:20
 * @description 接口RequestBody参数解密
 */
@EnableConfigurationProperties(SecureApiProperties.class)
@RestControllerAdvice
public class SecureApiRequestHandler implements RequestBodyAdvice {
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;
    private final RsaSignatureUtils rsaSignatureUtils;

    public SecureApiRequestHandler(SecureApiPropertiesConfig secureApiPropertiesConfig, RsaSignatureUtils rsaSignatureUtils) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
        this.rsaSignatureUtils = rsaSignatureUtils;
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (secureApiPropertiesConfig.isEnabled()) {
            Method method = methodParameter.getMethod();
            if (method != null) {
                Class<?> declaringClass = method.getDeclaringClass();
                if (declaringClass.isAnnotationPresent(DecryptApi.class) || method.isAnnotationPresent(DecryptApi.class)) {
                    return true;
                }
                return SecureApiThreadLocal.getIsDecryptApi() && !method.isAnnotationPresent(DecryptIgnore.class) && !declaringClass.isAnnotationPresent(DecryptIgnore.class);
            }
        }
        return false;
    }

    @Override
    @NonNull
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage, MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return new DecryptHttpInputMessage(parameter.getMethod(), inputMessage, secureApiPropertiesConfig, rsaSignatureUtils);
    }

    @Override
    @NonNull
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
