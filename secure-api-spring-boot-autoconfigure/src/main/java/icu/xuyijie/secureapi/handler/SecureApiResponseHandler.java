package icu.xuyijie.secureapi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xuyijie.secureapi.annotation.EncryptApi;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:20
 * @description 接口返回值加密
 */
@EnableConfigurationProperties(SecureApiProperties.class)
@RestControllerAdvice
public class SecureApiResponseHandler implements ResponseBodyAdvice<Object> {
    private static final Logger log = LoggerFactory.getLogger(SecureApiResponseHandler.class);

    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public SecureApiResponseHandler(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (secureApiPropertiesConfig.isEnabled()) {
            // 判断逻辑就是方法是否含有 @Encrypt 注解，如果有，表示该接口需要加密处理，如果没有，表示该接口不需要加密处理
            Class<?> declaringClass = returnType.getDeclaringClass();
            if (declaringClass.isAnnotationPresent(EncryptApi.class) || returnType.hasMethodAnnotation(EncryptApi.class)) {
                return true;
            }
            return SecureApiThreadLocal.getIsEncryptApi();
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            String bodyJson = new ObjectMapper().writeValueAsString(body);
            String encrypt = CipherModeHandler.handleEncryptMode(bodyJson, secureApiPropertiesConfig);
            if (secureApiPropertiesConfig.isShowLog()) {
                if (SecureApiProperties.Mode.COMMON == secureApiPropertiesConfig.getMode()) {
                    log.info("\n接口返回值加密\n方法：{}\n模式：{}\n加密算法：{}\n加密前：{}\n加密后：{}", returnType.getMethod(), secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), bodyJson, encrypt);
                } else {
                    log.info("\n接口返回值加密\n方法：{}\n模式：{}\n会话密钥算法：{}\n加密算法：{}\n加密前：{}\n加密后：{}", returnType.getMethod(), secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getSessionKeyCipherAlgorithm(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), bodyJson, encrypt);
                }
            }
            return encrypt;
        } catch (JsonProcessingException e) {
            log.error("返回值转换为Json对象失败，接口加密处理失败，返回原值：{}", body);
            return body;
        }
    }
}
