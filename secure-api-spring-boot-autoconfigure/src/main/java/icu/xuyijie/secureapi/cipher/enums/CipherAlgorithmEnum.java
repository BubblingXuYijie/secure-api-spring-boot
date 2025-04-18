package icu.xuyijie.secureapi.cipher.enums;

import icu.xuyijie.secureapi.cipher.utils.CipherUtils;
import icu.xuyijie.secureapi.cipher.model.RsaKeyPair;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author 徐一杰
 * @date 2023/11/15 16:34
 * @description 加密算法枚举
 */
public enum CipherAlgorithmEnum {
    /**
     * 加密算法(加密算法/反馈模式/自动填充方案)
     */
    AES_CBC_NO_PADDING(KeyGenAlgorithmEnum.AES, "AES/CBC/NoPadding", 16, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    AES_CBC_PKCS5(KeyGenAlgorithmEnum.AES, "AES/CBC/PKCS5Padding", 16, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    AES_ECB_NO_PADDING(KeyGenAlgorithmEnum.AES, "AES/ECB/NoPadding", 16, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    AES_ECB_PKCS5(KeyGenAlgorithmEnum.AES, "AES/ECB/PKCS5Padding", 16, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_CBC_NO_PADDING(KeyGenAlgorithmEnum.DES, "DES/CBC/NoPadding", 8, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_CBC_PKCS5(KeyGenAlgorithmEnum.DES, "DES/CBC/PKCS5Padding", 8, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_ECB_NO_PADDING(KeyGenAlgorithmEnum.DES, "DES/ECB/NoPadding", 8, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_ECB_PKCS5(KeyGenAlgorithmEnum.DES, "DES/ECB/PKCS5Padding", 8, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_EDE_CBC_NO_PADDING(KeyGenAlgorithmEnum.DES_EDE, "DESede/CBC/NoPadding", 8, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_EDE_CBC_PKCS5(KeyGenAlgorithmEnum.DES_EDE, "DESede/CBC/PKCS5Padding", 8, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_EDE_ECB_NO_PADDING(KeyGenAlgorithmEnum.DES_EDE, "DESede/ECB/NoPadding", 8, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    DES_EDE_ECB_PKCS5(KeyGenAlgorithmEnum.DES_EDE, "DESede/ECB/PKCS5Padding", 8, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    SM4_CBC_NO_PADDING(KeyGenAlgorithmEnum.SM4, "SM4/CBC/NoPadding", 16, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    SM4_CBC_PKCS5(KeyGenAlgorithmEnum.SM4, "SM4/CBC/PKCS5Padding", 16, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateCbcKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    SM4_ECB_NO_PADDING(KeyGenAlgorithmEnum.SM4, "SM4/ECB/NoPadding", 16, 16) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    SM4_ECB_PKCS5(KeyGenAlgorithmEnum.SM4, "SM4/ECB/PKCS5Padding", 16, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateEcbKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.symmetricDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    RSA_ECB_PKCS1(KeyGenAlgorithmEnum.RSA, "RSA/ECB/PKCS1Padding", 0, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateRsaKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.rsaEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.rsaDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    RSA_ECB_SHA1(KeyGenAlgorithmEnum.RSA, "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", 0, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateRsaKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.rsaEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
            return CipherAlgorithmEnum.rsaDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    RSA_ECB_SHA256(KeyGenAlgorithmEnum.RSA, "RSA/ECB/OAEPWithSHA-256AndMGF1Padding", 0, 0) {
        @Override
        public void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig) {
            CipherAlgorithmEnum.generateRsaKeyIfAbsent(this, secureApiPropertiesConfig);
        }

        @Override
        public String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
//            if (content.getBytes(StandardCharsets.UTF_8).length > 190) {
//                return CipherAlgorithmEnum.rsaGroupEncrypt(190, content, this, secureApiPropertiesConfig);
//            }
            return CipherAlgorithmEnum.rsaEncrypt(content, this, secureApiPropertiesConfig);
        }

        @Override
        public String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig) {
//            if (content.getBytes(StandardCharsets.UTF_8).length > 344) {
//                return CipherAlgorithmEnum.rsaGroupDecrypt(256, content, this, secureApiPropertiesConfig);
//            }
            return CipherAlgorithmEnum.rsaDecrypt(content, this, secureApiPropertiesConfig);
        }
    },
    ;

    private static final Logger log = LoggerFactory.getLogger(CipherAlgorithmEnum.class);

    /**
     * 根据加密方案获取密钥
     */
    private final KeyGenAlgorithmEnum keyGenEnum;

    /**
     * 加密算法/反馈模式/自动填充方案
     */
    private final String value;

    /**
     * 偏移量的字符数组长度，刚好等于fillLength
     */
    private final Integer ivLength;

    /**
     * 明文的<字符数组长度>需要补全为的倍数
     * 如果是NO_PADDING才会用到这个参数，我们手动补全，如果是选择了自动填充方案加密，那么jdk会自动补全
     */
    private final Integer fillLength;

    CipherAlgorithmEnum(KeyGenAlgorithmEnum keyGenEnum, String value, Integer ivLength, Integer fillLength) {
        this.keyGenEnum = keyGenEnum;
        this.value = value;
        this.ivLength = ivLength;
        this.fillLength = fillLength;
    }

    public KeyGenAlgorithmEnum getKeyGenEnum() {
        return keyGenEnum;
    }

    public String getValue() {
        return value;
    }

    public Integer getIvLength() {
        return ivLength;
    }

    public Integer getFillLength() {
        return fillLength;
    }

    /**
     * 如果加解密所需key不存在，则随机生成并保存到secureApiPropertiesConfig
     * @param secureApiPropertiesConfig 存储key
     */
    public abstract void generateKeyIfAbsent(SecureApiPropertiesConfig secureApiPropertiesConfig);

    public abstract String encrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig);

    public abstract String decrypt(String content, SecureApiPropertiesConfig secureApiPropertiesConfig);

    private static void generateEcbKeyIfAbsent(CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        if (!StringUtils.hasText(secureApiPropertiesConfig.getKey())) {
            CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
            String randomSecreteKey = cipherUtils.getRandomSecreteKey();
            log.info("\n您未配置接口加解密key，生成随机key，请妥善保存\nkey：{}", randomSecreteKey);
            secureApiPropertiesConfig.setKey(randomSecreteKey);
        }
    }

    private static void generateCbcKeyIfAbsent(CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        generateEcbKeyIfAbsent(cipherAlgorithmEnum, secureApiPropertiesConfig);
        if (!StringUtils.hasText(secureApiPropertiesConfig.getIv())) {
            CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
            String randomIv = cipherUtils.getRandomIv();
            log.info("\n您未配置接口加解密iv，生成随机iv，请妥善保存\niv：{}", randomIv);
            secureApiPropertiesConfig.setIv(randomIv);
        }
    }

    private static void generateRsaKeyIfAbsent(CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        if (!StringUtils.hasText(secureApiPropertiesConfig.getPublicKey()) || !StringUtils.hasText(secureApiPropertiesConfig.getPrivateKey())) {
            CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
            RsaKeyPair rsaKeyPair = cipherUtils.getRandomRsaKeyPair();
            secureApiPropertiesConfig.setPublicKey(rsaKeyPair.getPublicKey());
            secureApiPropertiesConfig.setPrivateKey(rsaKeyPair.getPrivateKey());
            log.info("\n您未配置接口加解密密钥对，生成随机密钥对，请妥善保存\n公钥：{}\n私钥：{}", rsaKeyPair.getPublicKey(), rsaKeyPair.getPrivateKey());
        }
    }

    private static String symmetricEncrypt(String content, CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
        return cipherUtils.encrypt(content, secureApiPropertiesConfig.getKey(), secureApiPropertiesConfig.getIv());
    }

    private static String rsaEncrypt(String content, CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
        return cipherUtils.encrypt(content, secureApiPropertiesConfig.getPublicKey());
    }

    private static String symmetricDecrypt(String content, CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
        return cipherUtils.decrypt(content, secureApiPropertiesConfig.getKey(), secureApiPropertiesConfig.getIv());
    }

    private static String rsaDecrypt(String content, CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        CipherUtils cipherUtils = new CipherUtils(cipherAlgorithmEnum, secureApiPropertiesConfig.isUrlSafe());
        return cipherUtils.decrypt(content, secureApiPropertiesConfig.getPrivateKey());
    }

//    private static String rsaGroupEncrypt(int splitSize, String content, CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
//        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
//        int length = bytes.length;
//        int split = length / splitSize;
//        if (length % splitSize != 0) {
//            split ++;
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 1; i <= split; i ++) {
//            byte[] splitBytes = new byte[splitSize];
//            System.arraycopy(bytes, Math.min(i * splitSize, length), splitBytes, 0, length);
//            String encrypt = cipherAlgorithmEnum.encrypt(new String(splitBytes, StandardCharsets.UTF_8), secureApiPropertiesConfig);
//            stringBuilder.append(encrypt);
//        }
//        return stringBuilder.toString();
//    }
//
//    private static String rsaGroupDecrypt(int splitSize, String content, CipherAlgorithmEnum cipherAlgorithmEnum, SecureApiPropertiesConfig secureApiPropertiesConfig) {
//        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
//        int length = bytes.length;
//        int split = length / splitSize;
//        if (length % splitSize != 0) {
//            split ++;
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 1; i <= split; i ++) {
//            byte[] splitBytes = new byte[splitSize];
//            System.arraycopy(bytes, Math.min(i * splitSize, length), splitBytes, 0, length);
//            String encrypt = cipherAlgorithmEnum.decrypt(new String(splitBytes, StandardCharsets.UTF_8), secureApiPropertiesConfig);
//            stringBuilder.append(encrypt);
//        }
//        return stringBuilder.toString();
//    }
}
