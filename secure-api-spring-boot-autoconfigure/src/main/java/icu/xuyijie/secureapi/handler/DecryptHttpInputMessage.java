package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.cipher.utils.RsaSignatureUtils;
import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.lang.NonNull;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author 徐一杰
 * @date 2024/6/19 0:13
 * @description 解密接口参数，这里选择实现HttpInputMessage来处理body参数，因为用拦截器处理body需要额外代码处理流只能读取一次的问题
 */
public class DecryptHttpInputMessage implements HttpInputMessage {
    private final Logger log = LoggerFactory.getLogger(DecryptHttpInputMessage.class);
    private final HttpHeaders httpHeaders;
    private final InputStream body;

    public DecryptHttpInputMessage(Method method, HttpInputMessage inputMessage, SecureApiPropertiesConfig secureApiPropertiesConfig, RsaSignatureUtils rsaSignatureUtils) {
        // 一般请求头里的内容不做加密解密处理
        httpHeaders = inputMessage.getHeaders();
        // 数字签名
        String signature = httpHeaders.getFirst("X-signature");
        // 取出请求的body
        String content;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputMessage.getBody()))) {
            content = bufferedReader
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.error("获取请求体失败", e);
            body = null;
            return;
        }

        // 去除json值两边的双引号
        if (content.length() > 1 && content.charAt(0) == '"' && content.charAt(content.length() - 1) == '"') {
            content = content.substring(0, content.length() - 1).replaceFirst("\"", "");
        }

        // 解密
        String decryptBody = CipherModeHandler.handleDecryptMode(content, secureApiPropertiesConfig);
        byte[] decryptBodyBytes = decryptBody.getBytes(StandardCharsets.UTF_8);
        body = new ByteArrayInputStream(decryptBodyBytes);

        // 数字签名校验
        boolean signVerify = true;
        if (secureApiPropertiesConfig.isSignEnabled()) {
            signVerify = rsaSignatureUtils.verify(decryptBodyBytes, signature);
            if (!signVerify) {
                throw new SecureApiException(ErrorEnum.SIGNATURE_ERROR);
            }
        }

        if (secureApiPropertiesConfig.isShowLog()) {
            if (SecureApiProperties.Mode.COMMON == secureApiPropertiesConfig.getMode()) {
                log.info("\n接口参数体解密\n方法：{}\n模式：{}\n解密算法：{}\n解密前：{}\n解密后：{}\n数字签名：{}\n校验结果：{}", method, secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), content, decryptBody, signature, signVerify);
            } else {
                log.info("\n接口参数体解密\n方法：{}\n模式：{}\n会话密钥算法：{}\n解密算法：{}\n解密前：{}\n解密后：{}\n数字签名：{}\n校验结果：{}", method, secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getSessionKeyCipherAlgorithm(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), content, decryptBody, signature, signVerify);
            }
        }
    }

    @Override
    @NonNull
    public InputStream getBody() {
        return body;
    }

    @Override
    @NonNull
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }
}
