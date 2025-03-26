package icu.xuyijie.secureapi.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;
import icu.xuyijie.secureapi.cipher.CipherUtils;
import icu.xuyijie.secureapi.cipher.RSASignatureUtils;
import icu.xuyijie.secureapi.config.ObjectMapperConfig;
import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 徐一杰
 * @date 2024/6/18 15:08
 * @description 自动配置类
 */
@EnableConfigurationProperties(SecureApiProperties.class)
@AutoConfiguration
public class SecureApiAutoConfigure {
    private final Logger log = LoggerFactory.getLogger(SecureApiAutoConfigure.class);
    private final SecureApiProperties secureApiProperties;

    public SecureApiAutoConfigure(SecureApiProperties secureApiProperties) {
        this.secureApiProperties = secureApiProperties;
    }

    @Bean
    @ConditionalOnMissingBean(SecureApiPropertiesConfig.class)
    public SecureApiPropertiesConfig secureApiPropertiesConfig() {
        SecureApiPropertiesConfig secureApiPropertiesConfig = new SecureApiPropertiesConfig();
        secureApiPropertiesConfig.setEnabled(secureApiProperties.isEnabled());
        secureApiPropertiesConfig.setSignEnabled(secureApiProperties.isSignEnabled());
        secureApiPropertiesConfig.setShowLog(secureApiProperties.isShowLog());
        secureApiPropertiesConfig.setUrlSafe(secureApiProperties.isUrlSafe());
        secureApiPropertiesConfig.setMode(secureApiProperties.getMode());
        secureApiPropertiesConfig.setCipherAlgorithmEnum(secureApiProperties.getCipherAlgorithm());
        secureApiPropertiesConfig.setSessionKeyCipherAlgorithm(secureApiProperties.getSessionKeyCipherAlgorithm());
        secureApiPropertiesConfig.setKey(secureApiProperties.getKey());
        secureApiPropertiesConfig.setIv(secureApiProperties.getIv());
        secureApiPropertiesConfig.setPublicKey(secureApiProperties.getPublicKey());
        secureApiPropertiesConfig.setPrivateKey(secureApiProperties.getPrivateKey());
        secureApiPropertiesConfig.setSignPublicKey(secureApiProperties.getSignPublicKey());
        secureApiPropertiesConfig.setSignPrivateKey(secureApiProperties.getSignPrivateKey());
        secureApiPropertiesConfig.setEncryptUrl(secureApiProperties.getEncryptUrl());
        secureApiPropertiesConfig.setDecryptUrl(secureApiProperties.getDecryptUrl());
        secureApiPropertiesConfig.setDateFormat(secureApiProperties.getDateFormat());
        secureApiPropertiesConfig.setLocalDateTimeFormat(secureApiProperties.getLocalDateTimeFormat());
        secureApiPropertiesConfig.setLocalDateFormat(secureApiProperties.getLocalDateFormat());
        secureApiPropertiesConfig.setLocalTimeFormat(secureApiProperties.getLocalTimeFormat());
        return secureApiPropertiesConfig;
    }

    @Bean
    @ConditionalOnBean({SecureApiPropertiesConfig.class})
    public CipherUtils cipherUtils(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        // 初始化CipherUtils
        CipherAlgorithmEnum cipherAlgorithmEnum = secureApiPropertiesConfig.getCipherAlgorithmEnum();
        CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
        // 开启接口加密，初始化各项参数，打印日志
        if (secureApiPropertiesConfig.isEnabled()) {
            // 检测加密模式配置是否正确
            SecureApiProperties.Mode mode = secureApiPropertiesConfig.getMode();
            boolean isAlgorithmError = !cipherAlgorithmEnum.getValue().startsWith("RSA") || secureApiPropertiesConfig.getSessionKeyCipherAlgorithm().getValue().startsWith("RSA");
            if (SecureApiProperties.Mode.SESSION_KEY == mode && isAlgorithmError) {
                throw new SecureApiException(ErrorEnum.SESSION_MODE_CONFIG_ERROR);
            }
            // 如果用户没有配置key，根据加密算法自动生成key并打印在控制台
            cipherAlgorithmEnum.generateKeyIfAbsent(secureApiPropertiesConfig);
            if (SecureApiProperties.Mode.COMMON == mode) {
                log.info("\n已开启接口加密\n日志打印：{}\n密文UrlSafe：{}\n模式：{}\n加解密算法：{}\n加密URL配置：{}\n解密URL配置：{}\nDate格式化：{}\nLocalDateTime格式化：{}\nLocalDate格式化：{}\nLocalTime格式化：{}\n数字签名：{}", secureApiPropertiesConfig.isShowLog(), secureApiPropertiesConfig.isUrlSafe(), mode, cipherAlgorithmEnum, secureApiPropertiesConfig.getEncryptUrl(), secureApiPropertiesConfig.getDecryptUrl(), secureApiPropertiesConfig.getDateFormat(), secureApiPropertiesConfig.getLocalDateTimeFormat(), secureApiPropertiesConfig.getLocalDateFormat(), secureApiPropertiesConfig.getLocalTimeFormat(), secureApiPropertiesConfig.isSignEnabled());
            } else {
                log.info("\n已开启接口加密\n日志打印：{}\n密文UrlSafe：{}\n模式：{}\n会话密钥算法：{}\n加解密算法：{}\n加密URL配置：{}\n解密URL配置：{}\nDate格式化：{}\nLocalDateTime格式化：{}\nLocalDate格式化：{}\nLocalTime格式化：{}\n数字签名：{}", secureApiPropertiesConfig.isShowLog(), secureApiPropertiesConfig.isUrlSafe(), mode, secureApiPropertiesConfig.getSessionKeyCipherAlgorithm(), cipherAlgorithmEnum, secureApiPropertiesConfig.getEncryptUrl(), secureApiPropertiesConfig.getDecryptUrl(), secureApiPropertiesConfig.getDateFormat(), secureApiPropertiesConfig.getLocalDateTimeFormat(), secureApiPropertiesConfig.getLocalDateFormat(), secureApiPropertiesConfig.getLocalTimeFormat(), secureApiPropertiesConfig.isSignEnabled());
            }
        }
        if(secureApiPropertiesConfig.isSignEnabled()){
            // 如果用户没有配置数字签名验证的公、私钥，自动生成key并打印在控制台
            RSASignatureUtils rsaSignatureUtils = new RSASignatureUtils(secureApiPropertiesConfig);
            rsaSignatureUtils.generateKeyPair();
        }
        return cipherUtils;
    }

    @Bean
    @ConditionalOnBean({SecureApiPropertiesConfig.class})
    public ObjectMapper secureApiObjectMapper(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        return new ObjectMapperConfig(secureApiPropertiesConfig).myObjectMapper();
    }
}
