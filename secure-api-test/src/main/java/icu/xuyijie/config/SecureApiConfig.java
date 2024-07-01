package icu.xuyijie.config;

import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;
import icu.xuyijie.secureapi.cipher.CipherUtils;
import icu.xuyijie.secureapi.cipher.RsaKeyPair;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author 徐一杰
 * @date 2024/6/18 19:09
 * @description SecureApi配置
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
        secureApiPropertiesConfig.setUrlSafe(true);
        secureApiPropertiesConfig.setShowLog(true);
        secureApiPropertiesConfig.setMode(SecureApiProperties.Mode.COMMON);
        secureApiPropertiesConfig.setSessionKeyCipherAlgorithm(CipherAlgorithmEnum.AES_ECB_PKCS5);
        secureApiPropertiesConfig.setCipherAlgorithmEnum(CipherAlgorithmEnum.RSA_ECB_SHA256);

        // 密钥可以不设置，组件会自动生成一个，并打印在控制台，如果需要手动生成，只需要使用组件提供的CipherUtils
        CipherUtils cipherUtils = new CipherUtils(CipherAlgorithmEnum.RSA_ECB_SHA256);
        // 因为我们选择的是非对称加密RSA，所以生成一个密钥对，getRandomRsaKeyPair可传入seed参数，在测试时可用于控制每次生成的密钥相同
        RsaKeyPair randomRsaKeyPair = cipherUtils.getRandomRsaKeyPair("1");
        // 把生成的密钥对设置到secureApiPropertiesConfig
        secureApiPropertiesConfig.setPublicKey(randomRsaKeyPair.getPublicKey());
        secureApiPropertiesConfig.setPrivateKey(randomRsaKeyPair.getPrivateKey());

        // 不需要使用url匹配功能可以删除掉下面两行，或者传入空数组
        secureApiPropertiesConfig.setEncryptUrl(new SecureApiProperties.UrlPattern(List.of("/**"), List.of()));
        secureApiPropertiesConfig.setDecryptUrl(new SecureApiProperties.UrlPattern(List.of("/**"), List.of("/secureApiTest/testForm")));
        return secureApiPropertiesConfig;
    }
}
