package icu.xuyijie.secureapi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import icu.xuyijie.secureapi.annotation.EncryptApi;
import icu.xuyijie.secureapi.annotation.EncryptIgnore;
import icu.xuyijie.secureapi.cipher.utils.RsaSignatureUtils;
import icu.xuyijie.secureapi.constant.HeaderConstant;
import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
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
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:20
 * @description 接口返回值加密
 */
@EnableConfigurationProperties(SecureApiProperties.class)
@RestControllerAdvice
public class SecureApiResponseHandler implements ResponseBodyAdvice<Object> {
    private final Logger log = LoggerFactory.getLogger(SecureApiResponseHandler.class);

    private final SecureApiPropertiesConfig secureApiPropertiesConfig;
    private final ObjectMapper secureApiObjectMapper;
    private final RsaSignatureUtils rsaSignatureUtils;

    public SecureApiResponseHandler(SecureApiPropertiesConfig secureApiPropertiesConfig, ObjectMapper secureApiObjectMapper, RsaSignatureUtils rsaSignatureUtils) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
        this.secureApiObjectMapper = secureApiObjectMapper;
        this.rsaSignatureUtils = rsaSignatureUtils;
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (secureApiPropertiesConfig.isEnabled()) {
            // 判断逻辑就是方法是否含有 @Encrypt 注解，如果有，表示该接口需要加密处理，如果没有，表示该接口不需要加密处理
            Class<?> declaringClass = returnType.getDeclaringClass();
            if (declaringClass.isAnnotationPresent(EncryptApi.class) || returnType.hasMethodAnnotation(EncryptApi.class)) {
                return true;
            }
            return SecureApiThreadLocal.getIsEncryptApi() && !returnType.hasMethodAnnotation(EncryptIgnore.class) && !declaringClass.isAnnotationPresent(EncryptIgnore.class);
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        try {
            // 方法返回值为void不处理
            if (body == null) {
                return null;
            }

            String bodyJson = secureApiObjectMapper.writeValueAsString(body);
            if (bodyJson == null) {
                return body;
            }

            // 有些类型转为json后会使用双引号包裹，给它去掉 (像时间类型这种，我们想要返回格式化后的时间必须用 secureApiObjectMapper 处理，所以为了方便所有类型都用 mapper 转换一遍)
            if (checkIsNoNeedObjectMapper(body)) {
                bodyJson = handleIllegelJsonString(bodyJson);
            }

            // 处理数字签名
            signData(bodyJson, response);

            // 处理加密
            String encrypt = handleEncrypt(bodyJson);

            // 处理日志打印
            printLog(bodyJson, encrypt, returnType);

            return encrypt;
        } catch (JsonProcessingException e) {
            log.error("返回值转换为Json对象失败，接口加密处理失败，原值：{}", body, e);
            throw new SecureApiException(ErrorEnum.ENCRYPT_ERROR);
        }
    }

    /**
     * 检查是否 不 需要使用ObjectMapper序列化
     * @param body 值
     * @return 是否 不 需要使用ObjectMapper序列化
     */
    private boolean checkIsNoNeedObjectMapper(Object body) {
        // 这些非 json 字符串经过 ObjectMapper 序列化后都会在首尾多个引号，后面要去掉才能反序列化成功，数值类型🙅，多了引号只是相当于转换成String了
        return body instanceof String || body instanceof Date || body instanceof Temporal;
    }

    /**
     * 处理非法 json 字符串
     * @param s 非法 json 字符串
     * @return 合法 json 字符串
     */
    private String handleIllegelJsonString(String s) {
        s = s.replaceFirst("\"", "");
        return s.substring(0, s.lastIndexOf("\""));
    }

    /**
     * 为数据生成数字签名
     * @param data 原始数据
     * @param response 响应
     */
    private void signData(String data, ServerHttpResponse response) {
        if (secureApiPropertiesConfig.isSignEnabled()) {
            // 为数据生成数字签名
            String sign = rsaSignatureUtils.sign(data.getBytes(StandardCharsets.UTF_8));
            // 设置响应头
            response.getHeaders().add(HeaderConstant.X_SIGNATURE, sign);
        }
    }

    /**
     * 处理加密，配置 responseBodyField 后只加密 data
     * @param bodyJson 返回值 json 字符串
     * @return 密文
     * @throws JsonProcessingException objectMapper转换异常
     */
    private String handleEncrypt(String bodyJson) throws JsonProcessingException {
        // 如果用户配置了 responseBodyField，那么只加密配置的字段
        String responseBodyDataField = secureApiPropertiesConfig.getResponseBodyDataField();
        if (responseBodyDataField != null) {
            // 反序列化
            JsonNode jsonNode = secureApiObjectMapper.readTree(bodyJson);
            if (jsonNode instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                JsonNode dataNode = objectNode.get(responseBodyDataField);
                // data 为 null 直接返回
                if (dataNode.isNull()) {
                    return bodyJson;
                }
                String dataJsonString = secureApiObjectMapper.writeValueAsString(dataNode);
                // 还是一样，一些数据类型经过 writeValueAsString 后要处理前后引号
                if (!dataNode.isNumber() && StringUtils.hasText(dataNode.asText())) {
                    dataJsonString = handleIllegelJsonString(dataJsonString);
                }
                // 只加密data字段
                String encryptData = CipherModeHandler.handleEncryptMode(dataJsonString, secureApiPropertiesConfig);
                objectNode.put(responseBodyDataField, encryptData);
                return secureApiObjectMapper.writeValueAsString(objectNode);
            } else {
                throw new SecureApiException(ErrorEnum.CLASS_LOAD_ERROR);
            }
        }
        return CipherModeHandler.handleEncryptMode(bodyJson, secureApiPropertiesConfig);
    }

    /**
     * 处理日志打印
     * @param bodyJson 原始数据
     * @param encrypt 密文
     * @param returnType 返回值详细数据
     */
    private void printLog(String bodyJson, String encrypt, MethodParameter returnType) {
        if (secureApiPropertiesConfig.isShowLog()) {
            if (SecureApiProperties.Mode.COMMON == secureApiPropertiesConfig.getMode()) {
                log.info("\n接口返回值加密\n方法：{}\n模式：{}\n加密算法：{}\n加密前：{}\n加密后：{}", returnType.getMethod(), secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), bodyJson, encrypt);
            } else {
                log.info("\n接口返回值加密\n方法：{}\n模式：{}\n会话密钥算法：{}\n加密算法：{}\n加密前：{}\n加密后：{}", returnType.getMethod(), secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getSessionKeyCipherAlgorithm(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), bodyJson, encrypt);
            }
        }
    }

}
