package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;
import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.springframework.util.StringUtils;

/**
 * @author 徐一杰
 * @date 2024/6/19 18:01
 * @description 处理不同加密方案
 */
class CipherModeHandler {
    CipherModeHandler() {

    }

    /**
     * 根据不同加密方案处理key的获取
     *
     * @param content 明文
     * @param secureApiPropertiesConfig key配置信息
     * @return 密文
     */
    public static String handleEncryptMode(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        if (!StringUtils.hasText(content)) {
            return content;
        }
        CipherAlgorithmEnum cipherAlgorithmEnum = secureApiPropertiesConfig.getCipherAlgorithmEnum();
        // 如果是会话密钥模式
        if (SecureApiProperties.Mode.SESSION_KEY == secureApiPropertiesConfig.getMode()) {
            // 使用会话密钥加密算法加密返回值
            cipherAlgorithmEnum = secureApiPropertiesConfig.getSessionKeyCipherAlgorithm();
        }
        return cipherAlgorithmEnum.encrypt(content, secureApiPropertiesConfig);
    }

    /**
     * 根据不同解密方案处理key的获取
     *
     * @param content 密文
     * @param secureApiPropertiesConfig key配置信息
     * @return 明文
     */
    public static String handleDecryptMode(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        if (!StringUtils.hasText(content)) {
            return content;
        }
        CipherAlgorithmEnum cipherAlgorithmEnum = secureApiPropertiesConfig.getCipherAlgorithmEnum();
        // 如果是会话密钥模式
        if (SecureApiProperties.Mode.SESSION_KEY == secureApiPropertiesConfig.getMode()) {
            String sessionKey = secureApiPropertiesConfig.getKey();
            if (!StringUtils.hasText(sessionKey)) {
                throw new SecureApiException(ErrorEnum.SESSION_KEY_EMPTY);
            }
            // 使用RSA私钥解密会话密钥
            try {
                String decryptSessionKey = cipherAlgorithmEnum.decrypt(sessionKey, secureApiPropertiesConfig);
                secureApiPropertiesConfig.setKey(decryptSessionKey);
            } catch (SecureApiException e) {
                throw new SecureApiException(ErrorEnum.SESSION_KEY_DECRYPT_ERROR);
            }
            // 接下来使用会话密钥的加密算法解密请求参数
            cipherAlgorithmEnum = secureApiPropertiesConfig.getSessionKeyCipherAlgorithm();
        }
        return cipherAlgorithmEnum.decrypt(content, secureApiPropertiesConfig);
    }
}
