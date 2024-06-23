package icu.xuyijie.secureapi.threadlocal;

/**
 * @author 徐一杰
 * @date 2024/6/21 15:25
 * @description
 */
public class SecureApiThreadLocal {
    private SecureApiThreadLocal() {

    }

    private static final ThreadLocal<Boolean> IS_ENCRYPT_API = new InheritableThreadLocal<>();
    private static final ThreadLocal<Boolean> IS_DECRYPT_API = new InheritableThreadLocal<>();
    static {
        IS_ENCRYPT_API.set(false);
        IS_DECRYPT_API.set(false);
    }

    /**
     * 设置 IS_ENCRYPT_API 的值
     *
     * @param isEncryptApi 布尔值
     */
    public static void setIsEncryptApi(boolean isEncryptApi) {
        IS_ENCRYPT_API.set(isEncryptApi);
    }

    /**
     * 查看 IS_ENCRYPT_API 的值
     */
    public static boolean getIsEncryptApi() {
        return IS_ENCRYPT_API.get();
    }

    /**
     * 清除 IS_ENCRYPT_API 的值
     */
    public static void clearIsEncryptApi() {
        IS_ENCRYPT_API.remove();
    }

    /**
     * 设置 IS_DECRYPT_API 的值
     *
     * @param isDecrypt 布尔值
     */
    public static void setIsDecryptApi(boolean isDecrypt) {
        IS_DECRYPT_API.set(isDecrypt);
    }

    /**
     * 查看 IS_DECRYPT_API 的值
     */
    public static boolean getIsDecryptApi() {
        return IS_DECRYPT_API.get();
    }

    /**
     * 清除 IS_DECRYPT_API 的值
     */
    public static void clearIsDecryptApi() {
        IS_DECRYPT_API.remove();
    }

}
