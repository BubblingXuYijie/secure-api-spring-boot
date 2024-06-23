package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.*;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

/**
 * @author 徐一杰
 * @date 2024/6/19 0:13
 * @description 解密接口参数
 */
public class DecryptHttpInputMessage implements HttpInputMessage {
    private static final Logger log = LoggerFactory.getLogger(DecryptHttpInputMessage.class);
    private final HttpHeaders httpHeaders;
    private final InputStream body;

    public DecryptHttpInputMessage(Method method, HttpInputMessage inputMessage, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        // 一般请求头里的内容不做加密解密处理
        httpHeaders = inputMessage.getHeaders();

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
        String decryptBody = content;
        // 判断是否是密文，大括号开头的是json，说明是明文，就不需要解密了
        if (!content.startsWith("{")) {
            // 去除json值两边的双引号
            content = content.replace("\"", "");
            decryptBody = CipherModeHandler.handleDecryptMode(content, secureApiPropertiesConfig);
        }
        body = new ByteArrayInputStream(decryptBody.getBytes());

        if (secureApiPropertiesConfig.isShowLog()) {
            if (SecureApiProperties.Mode.COMMON == secureApiPropertiesConfig.getMode()) {
                log.info("\n接口参数体解密\n方法：{}\n模式：{}\n解密算法：{}\n解密前：{}\n解密后：{}", method, secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), content, decryptBody);
            } else {
                log.info("\n接口参数体解密\n方法：{}\n模式：{}\n会话密钥算法：{}\n解密算法：{}\n解密前：{}\n解密后：{}", method, secureApiPropertiesConfig.getMode(), secureApiPropertiesConfig.getSessionKeyCipherAlgorithm(), secureApiPropertiesConfig.getCipherAlgorithmEnum(), content, decryptBody);
            }
        }
    }

    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }
}
