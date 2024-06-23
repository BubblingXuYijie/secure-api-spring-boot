package icu.xuyijie.secureapi.autoconfigure;

import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;
import icu.xuyijie.secureapi.cipher.CipherUtils;
import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    private static final Logger log = LoggerFactory.getLogger(SecureApiAutoConfigure.class);
    private final SecureApiProperties secureApiProperties;

    public SecureApiAutoConfigure(SecureApiProperties secureApiProperties) {
        this.secureApiProperties = secureApiProperties;
    }

    @Bean
    @ConditionalOnMissingBean(SecureApiPropertiesConfig.class)
    public SecureApiPropertiesConfig secureApiPropertiesConfig() {
        SecureApiPropertiesConfig secureApiPropertiesConfig = new SecureApiPropertiesConfig();
        BeanUtils.copyProperties(secureApiProperties, secureApiPropertiesConfig);
        return secureApiPropertiesConfig;
    }

    @Bean
    @ConditionalOnBean({SecureApiPropertiesConfig.class})
    public CipherUtils cipherUtils(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        // 初始化CipherUtils
        CipherAlgorithmEnum cipherAlgorithmEnum = secureApiPropertiesConfig.getCipherAlgorithmEnum();
        CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum);
        // 开启接口加密，初始化各项参数，打印日志
        if (secureApiPropertiesConfig.isEnabled()) {
            // 检测加密模式配置是否正确
            SecureApiProperties.Mode mode = secureApiPropertiesConfig.getMode();
            boolean isAlgorithmCorrect = !cipherAlgorithmEnum.getValue().startsWith("RSA") || secureApiPropertiesConfig.getSessionKeyCipherAlgorithm().getValue().startsWith("RSA");
            if (SecureApiProperties.Mode.SESSION_KEY == mode && isAlgorithmCorrect) {
                throw new SecureApiException(ErrorEnum.SESSION_MODE_CONFIG_ERROR);
            }
            // 如果用户没有配置key，根据加密算法自动生成key并打印在控制台
            cipherAlgorithmEnum.generateKeyIfAbsent(secureApiPropertiesConfig);
            if (SecureApiProperties.Mode.COMMON == mode) {
                log.info("\n已开启接口加密\n日志打印：{}\n模式：{}\n加解密算法：{}\n加密URL配置：{}\n解密URL配置：{}", secureApiPropertiesConfig.isShowLog(), mode, cipherAlgorithmEnum, secureApiPropertiesConfig.getEncryptUrl(), secureApiPropertiesConfig.getDecryptUrl());
            } else {
                log.info("\n已开启接口加密\n日志打印：{}\n模式：{}\n会话密钥算法：{}\n加解密算法：{}\n加密URL配置：{}\n解密URL配置：{}", secureApiPropertiesConfig.isShowLog(), mode, secureApiPropertiesConfig.getSessionKeyCipherAlgorithm(), cipherAlgorithmEnum, secureApiPropertiesConfig.getEncryptUrl(), secureApiPropertiesConfig.getDecryptUrl());
            }
        }
        return cipherUtils;
    }
}
