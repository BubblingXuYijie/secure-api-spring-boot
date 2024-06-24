package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.annotation.DecryptApi;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * @author 徐一杰
 * @date 2024/6/24 19:14
 * @description param参数处理器
 */
@Component
public class SecureApiArgumentResolver implements HandlerMethodArgumentResolver {
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public SecureApiArgumentResolver(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> declaringClass = parameter.getDeclaringClass();
        boolean declaringClassAnnotationPresent = declaringClass.isAnnotationPresent(DecryptApi.class);
        boolean hasMethodAnnotation = parameter.hasMethodAnnotation(DecryptApi.class);
        boolean parameterAnnotation = parameter.hasParameterAnnotation(DecryptApi.class);
        return SecureApiThreadLocal.getIsDecryptApi() || parameterAnnotation || declaringClassAnnotationPresent || hasMethodAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = parameter.getParameterName();
        return Optional.ofNullable(parameterName)
                .map(webRequest::getParameter)
                .map(s -> CipherModeHandler.handleDecryptMode(s, secureApiPropertiesConfig))
                .orElse(null);
    }
}
