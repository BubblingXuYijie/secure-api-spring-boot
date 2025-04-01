package icu.xuyijie.secureapi.model;

import icu.xuyijie.secureapi.cipher.enums.CipherAlgorithmEnum;

/**
 * @author 徐一杰
 * @date 2024/6/18 18:51
 * @description Bean方式配置文件
 */
public class SecureApiPropertiesConfig {
    /**
     * 是否开启接口加解密功能
     */
    private boolean enabled = false;

    /**
     * 是否开启数字签名校验功能
     */
    private boolean signEnabled = false;

    /**
     * 生成的base64是否是urlSafe的
     */
    private boolean isUrlSafe = true;

    /**
     * 是否打印加解密结果日志
     */
    private boolean showLog = true;

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
     * 数字签名公钥
     */
    private String signPublicKey;

    /**
     * 数字签名私钥
     */
    private String signPrivateKey;

    /**
     * 加密url配置
     */
    private SecureApiProperties.UrlPattern encryptUrl = new SecureApiProperties.UrlPattern();

    /**
     * 解密url配置
     */
    private SecureApiProperties.UrlPattern decryptUrl = new SecureApiProperties.UrlPattern();

    /**
     * json日期格式化
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private String localDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private String localDateFormat = "yyyy-MM-dd";
    private String localTimeFormat = "HH:mm:ss";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSignEnabled() {
        return signEnabled;
    }

    public void setSignEnabled(boolean signEnabled) {
        this.signEnabled = signEnabled;
    }


    public boolean isUrlSafe() {
        return isUrlSafe;
    }

    public void setUrlSafe(boolean urlSafe) {
        isUrlSafe = urlSafe;
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

    public String getSignPublicKey() {
        return signPublicKey;
    }

    public void setSignPublicKey(String signPublicKey) {
        this.signPublicKey = signPublicKey;
    }

    public String getSignPrivateKey() {
        return signPrivateKey;
    }

    public void setSignPrivateKey(String signPrivateKey) {
        this.signPrivateKey = signPrivateKey;
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

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getLocalDateTimeFormat() {
        return localDateTimeFormat;
    }

    public void setLocalDateTimeFormat(String localDateTimeFormat) {
        this.localDateTimeFormat = localDateTimeFormat;
    }

    public String getLocalDateFormat() {
        return localDateFormat;
    }

    public void setLocalDateFormat(String localDateFormat) {
        this.localDateFormat = localDateFormat;
    }

    public String getLocalTimeFormat() {
        return localTimeFormat;
    }

    public void setLocalTimeFormat(String localTimeFormat) {
        this.localTimeFormat = localTimeFormat;
    }

    @Override
    public String toString() {
        return "SecureApiPropertiesConfig{" +
                "enabled=" + enabled +
                ", signEnabled=" + signEnabled +
                ", isUrlSafe=" + isUrlSafe +
                ", showLog=" + showLog +
                ", mode=" + mode +
                ", sessionKeyCipherAlgorithm=" + sessionKeyCipherAlgorithm +
                ", cipherAlgorithmEnum=" + cipherAlgorithmEnum +
                ", key='" + key + '\'' +
                ", iv='" + iv + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", signPublicKey='" + signPublicKey + '\'' +
                ", signPrivateKey='" + signPrivateKey + '\'' +
                ", encryptUrl=" + encryptUrl +
                ", decryptUrl=" + decryptUrl +
                ", dateFormat='" + dateFormat + '\'' +
                ", localDateTimeFormat='" + localDateTimeFormat + '\'' +
                ", localDateFormat='" + localDateFormat + '\'' +
                ", localTimeFormat='" + localTimeFormat + '\'' +
                '}';
    }
}
