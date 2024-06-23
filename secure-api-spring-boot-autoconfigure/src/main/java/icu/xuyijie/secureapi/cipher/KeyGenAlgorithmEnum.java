package icu.xuyijie.secureapi.cipher;

/**
 * @author 徐一杰
 * @date 2023/11/15 16:57
 * @description 密钥生成算法枚举
 */
public enum KeyGenAlgorithmEnum {
    /**
     * KeyGenerator传入的密钥算法，第一个参数是getInstance值，第二个是init长度
     */
    AES("AES", 256),
    DES("DES", 56),
    DES_EDE("DESede", 168),
    RSA("RSA", 2048),
    ;

    /**
     * 密钥算法
     */
    private final String value;

    /**
     * 密钥长度，单位是bit，也就是说256的AES密钥长度是32个字节
     */
    private final Integer length;

    KeyGenAlgorithmEnum(String value, Integer length) {
        this.value = value;
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public Integer getLength() {
        return length;
    }
}
