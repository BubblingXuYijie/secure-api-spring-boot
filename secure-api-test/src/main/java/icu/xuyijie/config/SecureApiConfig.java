package icu.xuyijie.config;

import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 徐一杰
 * @date 2024/6/18 19:09
 * @description
 */
@SpringBootConfiguration
public class SecureApiConfig {
    /**
     * 这里的配置了Bean会导致yml配置的数据失效
     * @return SecureApiPropertiesConfig
     */
    @Bean
    public SecureApiPropertiesConfig secureApiPropertiesConfig() {
        SecureApiPropertiesConfig secureApiPropertiesConfig = new SecureApiPropertiesConfig();
        secureApiPropertiesConfig.setEnabled(true);
        secureApiPropertiesConfig.setShowLog(true);
        secureApiPropertiesConfig.setMode(SecureApiProperties.Mode.SESSION_KEY);
        secureApiPropertiesConfig.setSessionKeyCipherAlgorithm(CipherAlgorithmEnum.AES_ECB_PKCS5);
        secureApiPropertiesConfig.setCipherAlgorithmEnum(CipherAlgorithmEnum.RSA_ECB_SHA256);
        secureApiPropertiesConfig.setEncryptUrl(new SecureApiProperties.UrlPattern(Arrays.asList("/**"), new ArrayList<>()));
        secureApiPropertiesConfig.setDecryptUrl(new SecureApiProperties.UrlPattern(Arrays.asList("/**"), new ArrayList<>()));
        return secureApiPropertiesConfig;
    }
}
