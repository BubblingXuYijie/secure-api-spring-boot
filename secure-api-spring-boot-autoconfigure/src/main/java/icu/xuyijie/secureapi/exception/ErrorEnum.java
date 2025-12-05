package icu.xuyijie.secureapi.exception;

/**
 * @author 徐一杰
 * @date 2024/6/19 18:39
 * @description 错误枚举
 */
public enum ErrorEnum {
    /**
     * 未知错误
     */
    FAILURE("未知错误"),
    CONTENT_EMPTY("传入待加密字符串为空"),
    KEY_EMPTY("传入加密key为空"),
    ENCRYPT_ERROR("加密失败"),
    DECRYPT_ERROR("解密失败，请检查密文或加密key和解密key是否相同"),
    RSA_ENCRYPT_ERROR("RSA加密失败"),
    RSA_DECRYPT_ERROR("RSA解密失败，请检查密文或公钥和私钥匹配情况"),
    SESSION_KEY_EMPTY("会话密钥为空"),
    SESSION_KEY_DECRYPT_ERROR("会话密钥解密失败，请检查密文或公钥和私钥匹配情况"),
    KEY_ERROR("公钥或私钥错误"),
    KEY_CREATE_ERROR("获取密钥失败"),
    SESSION_MODE_CONFIG_ERROR("会话密钥模式中，必须设置加密算法为RSA，会话密钥算法为非RSA"),
    SIGNATURE_ERROR("数字签名校验失败"),
    CLASS_LOAD_ERROR("无法加载配置的返回体包路径"),
    ;

    private final String message;

    ErrorEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
