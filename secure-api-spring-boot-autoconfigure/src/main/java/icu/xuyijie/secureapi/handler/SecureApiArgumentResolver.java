package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.annotation.DecryptParam;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        boolean parameterAnnotation = parameter.hasParameterAnnotation(DecryptParam.class);
        // 三种情况走这个处理器：1、配置了解密url并且没有加@RequestParam注解。2、参数加了DecryptParam注解
        return SecureApiThreadLocal.getIsDecryptApi() || parameterAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = parameter.getParameterName();
        DecryptParam decryptParam = parameter.getParameterAnnotation(DecryptParam.class);
        boolean hasDecryptParam = decryptParam != null;
        // 存在注解重新设置参数名
        if (hasDecryptParam && StringUtils.hasText(decryptParam.value())) {
            parameterName = decryptParam.value();
        }
        // 得到参数Optional
        Optional<String> parameterNameOptional = Optional.ofNullable(parameterName)
                .map(webRequest::getParameter);
        // 参数为空并且DecryptParam注解要求参数必传，抛出异常
        if (!parameterNameOptional.isPresent() && hasDecryptParam && decryptParam.required()) {
            throw new MissingServletRequestParameterException(decryptParam.value(), parameter.getParameterType().getTypeName());
        }
        // 参数解密
        String parameterValue = parameterNameOptional
                .map(s -> CipherModeHandler.handleDecryptMode(s, secureApiPropertiesConfig))
                .orElse(null);
        // 参数解密成功
        if (StringUtils.hasText(parameterValue)) {
            return parameterValue;
        }
        // 参数解密失败或者参数为空不解密返回defaultValue
        if (hasDecryptParam) {
            return StringUtils.hasText(decryptParam.defaultValue()) ? decryptParam.defaultValue() : null;
        }
        // 没有设置defaultValue返回null
        return null;
    }
}
