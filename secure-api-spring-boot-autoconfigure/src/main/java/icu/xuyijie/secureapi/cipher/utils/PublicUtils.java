package icu.xuyijie.secureapi.cipher.utils;

import icu.xuyijie.secureapi.cipher.enums.KeyGenAlgorithmEnum;
import icu.xuyijie.secureapi.cipher.model.RsaKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author 徐一杰
 * @date 2025/04/01 14:11
 * @description 基础方法
 */
class PublicUtils {
    private final Logger log = LoggerFactory.getLogger(PublicUtils.class);

    /**
     * 生成的base64是否是urlSafe的
     */
    private final boolean isUrlSafe;

    public PublicUtils(boolean isUrlSafe) {
        this.isUrlSafe = isUrlSafe;
    }

    /**
     * 得到密钥字符串
     * @param keyPair KeyPair
     * @return 密钥字符串
     */
    public RsaKeyPair getRsaKeyPairByKeyPair(KeyPair keyPair) {
        // 得到密钥字符串
        String publicKeyString = byte2Base64(keyPair.getPublic().getEncoded());
        String privateKeyString = byte2Base64(keyPair.getPrivate().getEncoded());
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param seed 随机数种子
     * @return RsaKeyPair对象
     */
    public KeyPair getRandomKeyPair(String seed) {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KeyGenAlgorithmEnum.RSA.getValue());
            SecureRandom secureRandom = getSecureRandom(seed);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(KeyGenAlgorithmEnum.RSA.getLength(), secureRandom);
            // 生成一个密钥对，保存在keyPair中
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("获取RSA密钥对失败，使用默认密钥对", e);
        }
        return null;
    }

    /**
     * 根据随机数种子获取 SecureRandom
     *
     * @param seed 随机数种子
     * @return SecureRandom
     */
    public SecureRandom getSecureRandom(String seed) {
        try {
            // 此类提供加密的强随机数生成器 (RNG)，该实现在windows上每次生成的key都相同，但是在部分linux或solaris系统上则不同。
            // SecureRandom random = new SecureRandom(key.getBytes())
            // 指定算法名称，这样不同的系统上每次生成的key都是相同的
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            if (StringUtils.hasText(seed)) {
                secureRandom.setSeed(seed.getBytes(StandardCharsets.UTF_8));
            }
            return secureRandom;
        } catch (NoSuchAlgorithmException e) {
            log.error("获取SecureRandom实例错误", e);
        }
        return StringUtils.hasText(seed) ? new SecureRandom(seed.getBytes(StandardCharsets.UTF_8)) : new SecureRandom();
    }


    /**
     * base64 形式的 RSA 私钥转换为 PrivateKey 对象
     *
     * @param base64Key base64 形式的 RSA 私钥
     */
    public PrivateKey getPrivateKeyFromBase64(String base64Key) {
        // 1. 解码Base64字符串为字节数组
        byte[] decodedKey = base642Byte(base64Key);

        // 2. 创建PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        // 3. 获取KeyFactory实例并生成PrivateKey
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("base64私钥转换为PrivateKey失败", e);
        }
        return null;
    }

    /**
     * base64 形式的 RSA 公钥转换为 PublicKey 对象
     *
     * @param base64Key base64 形式的 RSA 公钥
     */
    public PublicKey getPublicKeyFromBase64(String base64Key) {
        // 1. 解码Base64字符串为字节数组
        byte[] decodedKey = base642Byte(base64Key);

        // 2. 创建X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        // 3. 获取KeyFactory实例并生成PublicKey
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("base64公钥转换为PublicKey失败", e);
        }
        return null;
    }

    /**
     * 字节数组转Base64编码
     *
     * @param bytes 字节数组
     * @return Base64
     */
    public String byte2Base64(byte[] bytes) {
        if (isUrlSafe) {
            return Base64.getUrlEncoder().encodeToString(bytes);
        } else {
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    /**
     * Base64编码转字节数组
     *
     * @param base64Str Base64
     * @return 字节数组
     */
    public byte[] base642Byte(String base64Str) {
        if (isUrlSafe) {
            return Base64.getUrlDecoder().decode(base64Str);
        } else {
            return Base64.getDecoder().decode(base64Str);
        }
    }

}
