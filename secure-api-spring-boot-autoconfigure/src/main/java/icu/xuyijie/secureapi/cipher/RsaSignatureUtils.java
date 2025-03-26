package icu.xuyijie.secureapi.cipher;

import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
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
 * @author LingfengHan、徐一杰
 * @date 2025/3/26 13:12
 * @description RSA数据签名校验工具类
 */
public class RsaSignatureUtils {

    private final Logger log = LoggerFactory.getLogger(RsaSignatureUtils.class);

    /**
     * 数字签名公钥
     */
    private PublicKey publicKey;

    /**
     * 数字签名私钥
     */
    private PrivateKey privateKey;

    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    // 改为实例方法而非静态方法，避免静态配置带来的潜在问题
    public RsaSignatureUtils(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    /**
     * 如果用户没有配置数字签名验证的公、私钥，自动生成key并打印在控制台
     */
    public void generateKeyIfAbsent() {
        generateKeyIfAbsent(null);
    }

    /**
     * 如果用户没有配置数字签名验证的公、私钥，自动生成key并打印在控制台，并把数字签名密钥对保存在全局
     */
    public void generateKeyIfAbsent(String seed) {
        // 用户没有设置数字签名密钥的情况
        if (!StringUtils.hasText(secureApiPropertiesConfig.getSignPublicKey()) || !StringUtils.hasText(secureApiPropertiesConfig.getSignPrivateKey())) {
            RsaKeyPair rsaKeyPair = getRandomRsaKeyPair(seed);
            secureApiPropertiesConfig.setSignPublicKey(rsaKeyPair.getPublicKey());
            secureApiPropertiesConfig.setSignPrivateKey(rsaKeyPair.getPrivateKey());
            log.info("\n您未配置数字签名密钥对，生成随机密钥对，请妥善保存\n数字签名公钥：{}\n数字签名私钥：{}", rsaKeyPair.getPublicKey(), rsaKeyPair.getPrivateKey());
        }
        // 检查一下数字签名密钥对是否被保存在了全局
        if (publicKey == null) {
            getAndSavePublicKeyFromBase64(secureApiPropertiesConfig.getSignPublicKey());
            getAndSavePrivateKeyFromBase64(secureApiPropertiesConfig.getSignPrivateKey());
        }
    }

    /**
     * base64 形式的 RSA 私钥转换为 PrivateKey 对象
     *
     * @param base64Key base64 形式的 RSA 私钥
     */
    public void getAndSavePrivateKeyFromBase64(String base64Key) {
        // 1. 解码Base64字符串为字节数组
        byte[] decodedKey = base642Byte(base64Key);

        // 2. 创建PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        // 3. 获取KeyFactory实例并生成PrivateKey
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("base64私钥转换为PrivateKey失败", e);
        }
    }

    /**
     * base64 形式的 RSA 公钥转换为 PublicKey 对象
     *
     * @param base64Key base64 形式的 RSA 公钥
     */
    public void getAndSavePublicKeyFromBase64(String base64Key) {
        // 1. 解码Base64字符串为字节数组
        byte[] decodedKey = base642Byte(base64Key);

        // 2. 创建X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        // 3. 获取KeyFactory实例并生成PublicKey
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("base64公钥转换为PublicKey失败", e);
        }
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param seed 随机数种子
     * @return RsaKeyPair对象
     */
    private RsaKeyPair getRandomRsaKeyPair(String seed) {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KeyGenAlgorithmEnum.RSA.getValue());
            SecureRandom secureRandom = getSecureRandom(seed);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(KeyGenAlgorithmEnum.RSA.getLength(), secureRandom);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            // 得到密钥字符串
            String publicKeyString = byte2Base64(publicKey.getEncoded());
            String privateKeyString = byte2Base64(privateKey.getEncoded());
            return new RsaKeyPair(publicKeyString, privateKeyString);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取RSA密钥对失败，使用默认密钥对", e);
        }
        return new RsaKeyPair();
    }

    /**
     * 根据随机数种子获取 SecureRandom
     *
     * @param seed 随机数种子
     * @return SecureRandom
     */
    private SecureRandom getSecureRandom(String seed) {
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

    public String sign(byte[] data) {
        return sign(data, null);
    }

    /**
     * 获取RSA数字签名
     *
     * @param data 待签名数据
     * @param seed 随机数种子
     * @return base64 数字签名
     */
    public String sign(byte[] data, String seed) {
        try {
            // 初始化Signature对象
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initSign(privateKey);
            // 更新数据
            signature.update(data);
            // 生成签名
            byte[] sign = signature.sign();
            return byte2Base64(sign);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error("获取RSA数字签名失败", e);
        }
        return null;
    }

    /**
     * RSA校验数字签名
     *
     * @param data   待校验数据
     * @param signed 数字签名
     * @return boolean 校验成功返回true，失败返回false
     */
    public boolean verify(byte[] data, String signed) {
        try {
            // 初始化Signature对象
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initVerify(publicKey);
            // 更新数据
            signature.update(data);
            // 验证签名
            return signature.verify(base642Byte(signed));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error("数字签名校验失败", e);
            throw new SecureApiException(ErrorEnum.SIGNATURE_ERROR);
        }
    }

    /**
     * 字节数组转Base64编码
     *
     * @param bytes 字节数组
     * @return Base64
     */
    private String byte2Base64(byte[] bytes) {
        if (secureApiPropertiesConfig.isUrlSafe()) {
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
    private byte[] base642Byte(String base64Str) {
        if (secureApiPropertiesConfig.isUrlSafe()) {
            return Base64.getUrlDecoder().decode(base64Str);
        } else {
            return Base64.getDecoder().decode(base64Str);
        }
    }

}