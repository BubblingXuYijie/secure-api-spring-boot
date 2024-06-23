package icu.xuyijie.secureapi.cipher;

import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author 徐一杰
 * @date 2023/11/15 16:09
 * @description 加密解密工具类
 */
public class CipherUtils {
    private final Logger log = LoggerFactory.getLogger(CipherUtils.class);

    /**
     * 加密算法
     */
    private final KeyGenAlgorithmEnum keyGenAlgorithmEnum;

    /**
     * 加密方案(加密算法/反馈模式/自动填充方案)
     */
    private final CipherAlgorithmEnum cipherAlgorithmEnum;

    /**
     * 定义字母和数字的字符集
     */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 随机填充字符串
     */
    private String paddingString = "";

    public CipherUtils() {
        this.keyGenAlgorithmEnum = KeyGenAlgorithmEnum.AES;
        this.cipherAlgorithmEnum = CipherAlgorithmEnum.AES_ECB_PKCS5;
    }

    public CipherUtils(CipherAlgorithmEnum cipherAlgorithmEnum) {
        this.keyGenAlgorithmEnum = cipherAlgorithmEnum.getKeyGenEnum();
        this.cipherAlgorithmEnum = cipherAlgorithmEnum;
    }

    public KeyGenAlgorithmEnum getKeyGenAlgorithmEnum() {
        return keyGenAlgorithmEnum;
    }

    public CipherAlgorithmEnum getCipherAlgorithmEnum() {
        return cipherAlgorithmEnum;
    }

    /**
     * 随机生成密钥
     *
     * @return 随机密钥
     */
    public String getRandomSecreteKey() {
        return getRandomSecreteKey(null);
    }

    /**
     * 随机生成密钥
     *
     * @param seed 随机数种子
     * @return 随机密钥
     */
    public String getRandomSecreteKey(String seed) {
        try {
            //返回生成指定算法密钥生成器的 KeyGenerator 对象
            KeyGenerator keyGen = KeyGenerator.getInstance(keyGenAlgorithmEnum.getValue());
            SecureRandom secureRandom = getSecureRandom(seed);
            //AES 要求密钥长度为 128/192/256，DES 56，DESede 168
            keyGen.init(keyGenAlgorithmEnum.getLength(), secureRandom);
            //生成一个密钥
            SecretKey secretKey = keyGen.generateKey();
            return byte2Base64(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            log.error("生成随机秘钥异常！");
        }
        return null;
    }

    /**
     * 随机生成iv
     *
     * @return 随机iv
     */
    public String getRandomIv() {
        return getRandomIv(null);
    }

    /**
     * 随机生成iv
     *
     * @param seed 随机数种子
     * @return 随机iv
     */
    public String getRandomIv(String seed) {
        return getRandomString(cipherAlgorithmEnum.getIvLength(), seed);
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length 长度
     * @param seed 随机数种子
     * @return 随机字符串
     */
    private String getRandomString(int length, String seed) {
        SecureRandom secureRandom = getSecureRandom(seed);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 随机生成RSA密钥对
     */
    public RsaKeyPair getRsaKeyPair() {
        return getRsaKeyPair(null);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param seed 随机数种子
     * @return RsaKeyPair对象
     */
    public RsaKeyPair getRsaKeyPair(String seed) {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KeyGenAlgorithmEnum.RSA.getValue());
            SecureRandom secureRandom = getSecureRandom(seed);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(KeyGenAlgorithmEnum.RSA.getLength(), secureRandom);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            // 得到密钥字符串
            String publicKeyString = byte2Base64(publicKey.getEncoded());
            String privateKeyString = byte2Base64(privateKey.getEncoded());
            return new RsaKeyPair(publicKeyString, privateKeyString);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取RSA密钥对失败，使用默认密钥对", e);
        }
        return new RsaKeyPair();
    }

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

    /**
     * 检查内容和key是否为空
     * @param content 内容
     * @param key 密钥
     */
    private void check(String content, String key) {
        if (!StringUtils.hasText(content)) {
            throw new SecureApiException(ErrorEnum.CONTENT_EMPTY);
        }
        if (!StringUtils.hasText(key)) {
            throw new SecureApiException(ErrorEnum.KEY_EMPTY);
        }
    }

    /**
     * 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public String encrypt(String content, String key) {
        check(content, key);
        switch (keyGenAlgorithmEnum) {
            case RSA:
                return encryptRsa(content, key);
            default:
                return encrypt(content, key, null);
        }
    }

    /**
     * RSA加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    private String encryptRsa(String content, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(cipherAlgorithmEnum.getValue());
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 初始化RSA加密模式的密码器，new X509EncodedKeySpec从byte[]恢复KeySpec对象
            PublicKey pubKey = KeyFactory.getInstance(keyGenAlgorithmEnum.getValue()).generatePublic(new X509EncodedKeySpec(base642Byte(key)));
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return byte2Base64(result);
        } catch (Exception e) {
            log.error("RSA加密失败", e);
            throw new SecureApiException(ErrorEnum.RSA_ENCRYPT_ERROR);
        }
    }

    /**
     * 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @param iv      偏移量
     * @return 返回Base64转码后的加密数据
     */
    public String encrypt(String content, String key, String iv) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(cipherAlgorithmEnum.getValue());
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 初始化为加密模式的密码器，ECB模式不设置iv
            if (StringUtils.hasText(iv)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
                cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(key), ivParameterSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(key));
            }
            //处理补全字符数组
            //byteContent = handlePadding(byteContent)
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return byte2Base64(result);
        } catch (Exception e) {
            log.error("加密失败", e);
            throw new SecureApiException(ErrorEnum.ENCRYPT_ERROR);
        }
    }

    /**
     * 处理补全明文字符数组
     *
     * @param byteContent 要加密的字符数组
     * @return 补全后的字符数组
     */
    private byte[] handlePadding(byte[] byteContent) {
        if (byteContent == null || byteContent.length == 0 || cipherAlgorithmEnum.getFillLength() == 0 || byteContent.length % cipherAlgorithmEnum.getFillLength() == 0) {
            //不需要补全，则清除补全字符串，防止解密污染
            paddingString = "";
            return byteContent;
        }
        //要补全的长度
        int paddingSize = cipherAlgorithmEnum.getFillLength() - byteContent.length % cipherAlgorithmEnum.getFillLength();
        //把原数组copy一份
        byte[] finalByteContent = Arrays.copyOf(byteContent, byteContent.length + paddingSize);
        //生成要补全长度的英文字符串
        paddingString = getRandomString(paddingSize, null);
        byte[] bytes = paddingString.getBytes(StandardCharsets.UTF_8);
        //合并数组
        System.arraycopy(bytes, 0, finalByteContent, byteContent.length, bytes.length);
        log.info("明文byte长度不符合要求，且加密模式为NO_PADDING，使用随机字符串自动补全，随机字符串：{}", paddingString);
        return finalByteContent;
    }

    /**
     * 解密操作
     *
     * @param content 解密内容
     * @param key     密钥
     * @return 解密结果
     */
    public String decrypt(String content, String key) {
        check(content, key);
        switch (keyGenAlgorithmEnum) {
            case RSA:
                return decryptRsa(content, key);
            default:
                return decrypt(content, key, null);
        }
    }

    /**
     * 解密操作
     *
     * @param content 解密内容
     * @param key     密钥
     * @param iv      偏移量
     * @return 解密结果
     */
    public String decrypt(String content, String key, String iv) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(cipherAlgorithmEnum.getValue());
            //使用密钥初始化，设置为解密模式，ECB模式不设置iv
            if (StringUtils.hasText(iv)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
                cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(key), ivParameterSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(key));
            }
            //执行操作
            byte[] result = cipher.doFinal(base642Byte(content));
            //去除最后匹配到的补全字符
            return new String(result, StandardCharsets.UTF_8).replaceAll(paddingString + "$", "");
        } catch (Exception e) {
            log.error("解密失败，请检查加密key和解密key是否相同", e);
            throw new SecureApiException(ErrorEnum.DECRYPT_ERROR);
        }
    }

    /**
     * 解密操作
     *
     * @param content 解密内容
     * @param key     密钥
     * @return 解密结果
     */
    private String decryptRsa(String content, String key) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(cipherAlgorithmEnum.getValue());
            //使用密钥初始化，设置为解密模式
            PrivateKey priKey = KeyFactory.getInstance(keyGenAlgorithmEnum.getValue()).generatePrivate(new PKCS8EncodedKeySpec(base642Byte(key)));
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            //执行操作
            byte[] result = cipher.doFinal(base642Byte(content));
            //去除最后匹配到的补全字符
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA解密失败，请检查公钥和私钥匹配情况", e);
            throw new SecureApiException(ErrorEnum.RSA_DECRYPT_ERROR);
        }
    }

    /**
     * 获取SecretKeySpec
     *
     * @return SecretKeySpec
     */
    private SecretKeySpec getSecretKeySpec(String key) {
        // 转换为密钥
        return new SecretKeySpec(base642Byte(key), 0, keyGenAlgorithmEnum.getLength() / 8, keyGenAlgorithmEnum.getValue());
    }

    /**
     * 字节数组转Base64编码
     *
     * @param bytes 字节数组
     * @return Base64
     */
    private String byte2Base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64编码转字节数组
     *
     * @param base64Str Base64
     * @return 字节数组
     */
    private byte[] base642Byte(String base64Str) {
        return Base64.getDecoder().decode(base64Str);
    }

}
