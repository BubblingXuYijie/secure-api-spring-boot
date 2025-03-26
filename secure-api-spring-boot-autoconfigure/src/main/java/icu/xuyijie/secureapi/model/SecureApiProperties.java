package icu.xuyijie.secureapi.model;

import icu.xuyijie.secureapi.cipher.CipherAlgorithmEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐一杰
 * @date 2024/6/18 15:08
 * @description yml方式配置文件
 */
@ConfigurationProperties(prefix = "secure-api")
public class SecureApiProperties {
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
    private Mode mode = Mode.COMMON;

    /**
     * 会话密钥算法
     */
    private CipherAlgorithmEnum sessionKeyCipherAlgorithm = CipherAlgorithmEnum.AES_ECB_PKCS5;

    /**
     * 加密算法
     */
    private CipherAlgorithmEnum cipherAlgorithm = CipherAlgorithmEnum.AES_ECB_PKCS5;

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
    private UrlPattern encryptUrl = new UrlPattern();

    /**
     * 解密url配置
     */
    private UrlPattern decryptUrl = new UrlPattern();

    /**
     * json日期格式化
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private String localDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private String localDateFormat = "yyyy-MM-dd";
    private String localTimeFormat = "HH:mm:ss";

    public static class UrlPattern {
        private List<String> includeUrls = new ArrayList<>();
        private List<String> excludeUrls = new ArrayList<>();

        public UrlPattern() {
        }

        public UrlPattern(List<String> includeUrls, List<String> excludeUrls) {
            this.includeUrls = includeUrls;
            this.excludeUrls = excludeUrls;
        }

        public List<String> getIncludeUrls() {
            return includeUrls;
        }

        public void setIncludeUrls(List<String> includeUrls) {
            this.includeUrls = includeUrls;
        }

        public List<String> getExcludeUrls() {
            return excludeUrls;
        }

        public void setExcludeUrls(List<String> excludeUrls) {
            this.excludeUrls = excludeUrls;
        }

        @Override
        public String toString() {
            return "UrlPattern{" +
                    "includeUrls=" + includeUrls +
                    ", excludeUrls=" + excludeUrls +
                    '}';
        }
    }

    public enum Mode {
        /**
         * 正常模式（默认）
         */
        COMMON,
        /**
         * 会话密钥模式（需要前端每次传入一个会话密钥）
         */
        SESSION_KEY
    }

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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public CipherAlgorithmEnum getSessionKeyCipherAlgorithm() {
        return sessionKeyCipherAlgorithm;
    }

    public void setSessionKeyCipherAlgorithm(CipherAlgorithmEnum sessionKeyCipherAlgorithm) {
        this.sessionKeyCipherAlgorithm = sessionKeyCipherAlgorithm;
    }

    public CipherAlgorithmEnum getCipherAlgorithm() {
        return cipherAlgorithm;
    }

    public void setCipherAlgorithm(CipherAlgorithmEnum cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
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

    public UrlPattern getEncryptUrl() {
        return encryptUrl;
    }

    public void setEncryptUrl(UrlPattern encryptUrl) {
        this.encryptUrl = encryptUrl;
    }

    public UrlPattern getDecryptUrl() {
        return decryptUrl;
    }

    public void setDecryptUrl(UrlPattern decryptUrl) {
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
        return "SecureApiProperties{" +
                "enabled=" + enabled +
                ", signEnabled=" + signEnabled +
                ", isUrlSafe=" + isUrlSafe +
                ", showLog=" + showLog +
                ", mode=" + mode +
                ", sessionKeyCipherAlgorithm=" + sessionKeyCipherAlgorithm +
                ", cipherAlgorithm=" + cipherAlgorithm +
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
