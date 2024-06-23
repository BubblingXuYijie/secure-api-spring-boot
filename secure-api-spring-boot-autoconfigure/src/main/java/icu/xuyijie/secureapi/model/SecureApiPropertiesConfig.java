package icu.xuyijie.secureapi.model;

import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:51
 * @description Bean方式配置文件
 */
public class SecureApiPropertiesConfig {
    /**
     * 是否开启接口加解密功能
     */
    private boolean enabled = true;

    /**
     * 是否打印加解密结果日志
     */
    private boolean showLog = false;

    /**
     * 加密模式
     */
    private SecureApiProperties.Mode mode = SecureApiProperties.Mode.COMMON;

    /**
     * 会话密钥算法
     */
    private CipherAlgorithmEnum sessionKeyCipherAlgorithm = CipherAlgorithmEnum.AES_ECB_PKCS5;

    /**
     * 加密算法
     */
    private CipherAlgorithmEnum cipherAlgorithmEnum = CipherAlgorithmEnum.AES_ECB_PKCS5;

    /**
     * 对称加密key
     */
    private String key;

    /**
     * 对称加密CBC模式偏移量
     */
    private String iv;

    /**
     * RSA算法时需要配置的公钥
     */
    private String publicKey;

    /**
     * RSA算法时需要配置的私钥
     */
    private String privateKey;

    /**
     * 加密url配置
     */
    private SecureApiProperties.UrlPattern encryptUrl = new SecureApiProperties.UrlPattern();

    /**
     * 解密url配置
     */
    private SecureApiProperties.UrlPattern decryptUrl = new SecureApiProperties.UrlPattern();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public SecureApiProperties.Mode getMode() {
        return mode;
    }

    public void setMode(SecureApiProperties.Mode mode) {
        this.mode = mode;
    }

    public CipherAlgorithmEnum getSessionKeyCipherAlgorithm() {
        return sessionKeyCipherAlgorithm;
    }

    public void setSessionKeyCipherAlgorithm(CipherAlgorithmEnum sessionKeyCipherAlgorithm) {
        this.sessionKeyCipherAlgorithm = sessionKeyCipherAlgorithm;
    }

    public CipherAlgorithmEnum getCipherAlgorithmEnum() {
        return cipherAlgorithmEnum;
    }

    public void setCipherAlgorithmEnum(CipherAlgorithmEnum cipherAlgorithmEnum) {
        this.cipherAlgorithmEnum = cipherAlgorithmEnum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public SecureApiProperties.UrlPattern getEncryptUrl() {
        return encryptUrl;
    }

    public void setEncryptUrl(SecureApiProperties.UrlPattern encryptUrl) {
        this.encryptUrl = encryptUrl;
    }

    public SecureApiProperties.UrlPattern getDecryptUrl() {
        return decryptUrl;
    }

    public void setDecryptUrl(SecureApiProperties.UrlPattern decryptUrl) {
        this.decryptUrl = decryptUrl;
    }

    @Override
    public String toString() {
        return "SecureApiPropertiesConfig{" +
                "enabled=" + enabled +
                ", showLog=" + showLog +
                ", mode=" + mode +
                ", sessionKeyCipherAlgorithm=" + sessionKeyCipherAlgorithm +
                ", cipherAlgorithmEnum=" + cipherAlgorithmEnum +
                ", key='" + key + '\'' +
                ", iv='" + iv + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", encryptUrl=" + encryptUrl +
                ", decryptUrl=" + decryptUrl +
                '}';
    }
}
