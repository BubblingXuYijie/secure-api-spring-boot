package icu.xuyijie.secureapi.cipher.utils;

import icu.xuyijie.secureapi.cipher.model.RsaKeyPair;
import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.security.*;

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
    private final PublicUtils publicUtils;

    // 改为实例方法而非静态方法，避免静态配置带来的潜在问题
    public RsaSignatureUtils(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
        this.publicUtils = new PublicUtils(secureApiPropertiesConfig.isUrlSafe());
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
            // 随机生成密钥对
            KeyPair keyPair  = publicUtils.getRandomKeyPair(seed);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            // 得到密钥字符串
            RsaKeyPair rsaKeyPair = publicUtils.getRsaKeyPairByKeyPair(keyPair);
            // 设置到组件中
            secureApiPropertiesConfig.setSignPublicKey(rsaKeyPair.getPublicKey());
            secureApiPropertiesConfig.setSignPrivateKey(rsaKeyPair.getPrivateKey());
            log.info("\n您未配置数字签名密钥对，生成随机密钥对，请妥善保存\n数字签名公钥：{}\n数字签名私钥：{}", rsaKeyPair.getPublicKey(), rsaKeyPair.getPrivateKey());
        }
        // 检查一下数字签名密钥对是否被保存在了全局
        if (publicKey == null) {
            publicKey = publicUtils.getPublicKeyFromBase64(secureApiPropertiesConfig.getSignPublicKey());
            privateKey = publicUtils.getPrivateKeyFromBase64(secureApiPropertiesConfig.getSignPrivateKey());
        }
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
            return publicUtils.byte2Base64(sign);
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
            return signature.verify(publicUtils.base642Byte(signed));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error("数字签名校验失败", e);
            throw new SecureApiException(ErrorEnum.SIGNATURE_ERROR);
        }
    }

}